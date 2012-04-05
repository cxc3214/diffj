#!/usr/bin/jruby -w
# -*- ruby -*-

require 'rubygems'
require 'riel'
require 'java'
require 'diffj/ast/item'
require 'diffj/ast/function'

include Java

import org.incava.diffj.MethodDiff
import org.incava.pmdx.ParameterUtil
import org.incava.pmdx.MethodUtil

module DiffJ
  class MethodComparator < MethodDiff
    include Loggable, FunctionComparator

    METHOD_BLOCK_ADDED = "method block added"
    METHOD_BLOCK_REMOVED = "method block removed"

    VALID_MODIFIERS = [
      ::Java::net.sourceforge.pmd.ast.JavaParserConstants::ABSTRACT,
      ::Java::net.sourceforge.pmd.ast.JavaParserConstants::FINAL,
      ::Java::net.sourceforge.pmd.ast.JavaParserConstants::NATIVE,
      ::Java::net.sourceforge.pmd.ast.JavaParserConstants::STATIC,
      ::Java::net.sourceforge.pmd.ast.JavaParserConstants::STRICTFP
    ]
    
    def initialize diffs
      super

      # fake superclass, for now:
      @itemcomp = ItemComparator.new diffs
    end

    def compare_access_xxx from, to
      info "from: #{from}"
      info "to  : #{to}"
      @itemcomp.compare_access from, to
    end

    def compare_modifiers_xxx from, to
      @itemcomp.compare_modifiers SimpleNodeUtil.getParent(from), SimpleNodeUtil.getParent(to), VALID_MODIFIERS
    end

    def method_compare_parameters_xxx from, to
      from_params = MethodUtil.getParameters from
      to_params = MethodUtil.getParameters to
      
      # should be calling super:
      function_compare_parameters_xxx from_params, to_params
    end

    def method_compare_throws_xxx from, to
      from_list = MethodUtil.getThrowsList from
      to_list = MethodUtil.getThrowsList to

      function_compare_throws_xxx from, from_list, to, to_list
    end

    def method_get_block node
      SimpleNodeUtil.findChild node, "net.sourceforge.pmd.ast.ASTBlock"
    end
    
    def method_compare_bodies_xxx from, to
      from_block = method_get_block from
      to_block = method_get_block to

      if from_block.nil?
        if to_block
          changed from, to, METHOD_BLOCK_ADDED
        end
      elsif to_block.nil?
        changed from, to, METHOD_BLOCK_REMOVED
      else
        from_name = MethodUtil.getFullName from
        to_name = MethodUtil.getFullName to
            
        compareBlocks from_name, from_block, to_name, to_block
      end
    end

    def compare_xxx from, to
      info "from: #{from}".on_red
      info "to  : #{to}".on_red

      compare_modifiers_xxx from, to
      compare_return_types_xxx from, to
      method_compare_parameters_xxx from, to

      method_compare_throws_xxx from, to
      method_compare_bodies_xxx from, to
    end
  end
end
