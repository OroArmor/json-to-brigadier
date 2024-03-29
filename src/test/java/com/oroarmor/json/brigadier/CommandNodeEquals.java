/*
 * MIT License
 *
 * Copyright (c) 2021 OroArmor (Eli Orona)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.oroarmor.json.brigadier;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;

public class CommandNodeEquals {
    public static <S> boolean equals(CommandNode<S> node, CommandNode<S> otherNode) {
        if (node.getName().equals(otherNode.getName())) {
            if (node instanceof ArgumentCommandNode<S, ?> nodeArgument && otherNode instanceof ArgumentCommandNode<S, ?> otherNodeArgument) {
                if (!nodeArgument.getType().equals(otherNodeArgument.getType())) {
                    return false;
                }
            }
            return bothHaveCommands(node, otherNode) && bothHaveRequires(node, otherNode) && childrenAreEqual(node, otherNode);
        }
        return false;
    }

    private static <S> boolean childrenAreEqual(CommandNode<S> node, CommandNode<S> otherNode) {
        if (node.getChildren().size() != otherNode.getChildren().size()) {
            return false;
        }

        List<CommandNode<S>> nodeChildren = new ArrayList<>(node.getChildren());
        List<CommandNode<S>> otherNodeChildren = new ArrayList<>(otherNode.getChildren());
        for (int i = 0; i < nodeChildren.size(); i++) {
            if (!equals(nodeChildren.get(i), otherNodeChildren.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static <S> boolean bothHaveCommands(CommandNode<S> node, CommandNode<S> otherNode) {
        return (node.getCommand() != null) == (otherNode.getCommand() != null);
    }

    private static <S> boolean bothHaveRequires(CommandNode<S> node, CommandNode<S> otherNode) {
        return (node.getRequirement() != null) == (otherNode.getRequirement() != null);
    }
}
