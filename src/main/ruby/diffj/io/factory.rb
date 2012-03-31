#!/usr/bin/jruby -w
# -*- ruby -*-

require 'rubygems'
require 'riel'
require 'java'
require 'diffj/ast'
require 'diffj/io/directory'
require 'diffj/io/file'

include Java

import org.incava.diffj.DiffJException

module DiffJ
  module IO
    class Factory
      include Loggable
      
      def create_element file, label, source, recurse
        info "file: #{file}"
        javafile = create_file file, label, source
        if javafile
          javafile
        elsif file.directory?
          Directory.new file, source, recurse
        else
          no_such_file file, label
          nil
        end
      end

      def create_file file, label, source
        if file.nil? || file.name == "-" || (file.file? && verify_exists(file, label))
          File.new file, label, nil, source
        else
          nil
        end
      end

      def verify_exists file, label
        if file && file.exists
          true
        else
          no_such_file file, label
          false
        end
      end

      def no_such_file file, label
        raise DiffJException.new(name(file, label) + " does not exist")
      end

      def name file, label
        label || file.absolute_path
      end
    end
  end
end