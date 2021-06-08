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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

/**
 * Parses Brigadier commands into JSON
 */
public final class BrigadierToJson {
    private static final JsonObject ROOT_ARGUMENT;

    static {
        ROOT_ARGUMENT = new JsonObject();
        ROOT_ARGUMENT.addProperty(StringConstants.TYPE, "brigadier:root");
    }

    /**
     * Parses a {@link com.mojang.brigadier.CommandDispatcher} into JSON. The root command is listed.
     *
     * @param dispatcher The path to the JSON file
     * @param <T>        The command context type
     * @return An string of JSON
     */
    public static <T> JsonObject parseObject(CommandDispatcher<T> dispatcher) {
        JsonObject root = new JsonObject();
        root.addProperty(StringConstants.NAME, "__root__");
        root.add(StringConstants.ARGUMENT, ROOT_ARGUMENT);

        JsonArray array = new JsonArray();
        for (CommandNode<T> commandNode : dispatcher.getRoot().getChildren()) {
            array.add(parseObject(commandNode));
        }
        root.add(StringConstants.CHILDREN, array);

        return root;
    }

    /**
     * Parses a {@link com.mojang.brigadier.CommandDispatcher} into JSON. The root command is listed.
     *
     * @param dispatcher The path to the JSON file
     * @param <T>        The command context type
     * @return An string of JSON
     */
    public static <T> String parse(CommandDispatcher<T> dispatcher) {
        return parse(dispatcher).toString();
    }

    /**
     * Parses a command node to JSON
     *
     * @param node The command node to convert to JSON
     * @param <T>  The command context type
     * @return An {@link ArgumentBuilder} for the JSON file
     */
    public static <T> String parse(CommandNode<T> node) {
        return parseObject(node).toString();
    }

    /**
     * Parses a command node to JSON
     *
     * @param node The command node to convert to JSON
     * @param <T>  The command context type
     * @return An {@link ArgumentBuilder} for the JSON file
     */
    public static <T> JsonObject parseObject(CommandNode<T> node) {
        JsonObject object = new JsonObject();
        object.addProperty(StringConstants.NAME, node.getName());

        JsonObject argument = new JsonObject();

        if (node instanceof LiteralCommandNode<T> literalCommandNode) {
            argument.addProperty(StringConstants.TYPE, "brigadier:literal");
        } else if (node instanceof ArgumentCommandNode<T, ?> argumentCommandNode) {
            BrigadierArgumentParsers.get((Class<? extends ArgumentType<?>>) argumentCommandNode.getType().getClass()).parse(argument, argumentCommandNode.getType());
        }

        object.add(StringConstants.ARGUMENT, argument);

        if (node.getChildren().size() > 0) {
            JsonArray array = new JsonArray();
            for (CommandNode<T> commandNode : node.getChildren()) {
                array.add(parseObject(commandNode));
            }
            object.add(StringConstants.CHILDREN, array);
        }

        if (node.getCommand() != null) {
            String value = node.getCommand().toString();
            if (value.matches("[\\w\\.]*::\\w*")) {
                object.addProperty(StringConstants.EXECUTES, value);
            } else {
                object.addProperty(StringConstants.EXECUTES, "Unable to parse method. Method is in class " + value.split("\\$\\$")[0]);
            }
        }

        if (node.getRequirement() != null) {
            String value = node.getRequirement().toString();
            if (value.matches("[\\w\\.]*::\\w*")) {
                object.addProperty(StringConstants.REQUIRES, value);
            } else {
                object.addProperty(StringConstants.REQUIRES, "Unable to parse method. Method is in class " + value.split("\\$\\$")[0]);
            }
        }

        return object;
    }
}
