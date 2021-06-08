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

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import org.junit.jupiter.api.Test;

import static com.mojang.brigadier.arguments.DoubleArgumentType.doubleArg;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestComplexCommand {
    private static boolean runSuccessful = false;

    public static int runCommandIntegerDefault(CommandContext<Object> o) {
        runSuccessful = true;
        return 1;
    }

    public static int runCommandInteger(CommandContext<Object> o) {
        runSuccessful = true;
        return 1;
    }

    public static int runCommandDoubleDefault(CommandContext<Object> o) {
        runSuccessful = true;
        return 1;
    }

    public static int runCommandDouble(CommandContext<Object> o) {
        runSuccessful = true;
        return 1;
    }

    public static boolean requires(Object o) {
        return o != null;
    }

    @Test
    public void testParse() throws URISyntaxException {
        CommandNode<Object> manualCommandNode = literal("test")
                .then(literal("integer")
                        .then(argument("value", integer(0, 1))
                                .executes(TestComplexCommand::runCommandDouble))
                        .executes(TestComplexCommand::runCommandDoubleDefault))
                .then(literal("double")
                        .then(argument("value", doubleArg(0, 1))
                                .executes(TestComplexCommand::runCommandDouble))
                        .executes(TestComplexCommand::runCommandDoubleDefault))
                .build();

        CommandNode<Object> jsonCommandNode = JsonToBrigadier.parse(Paths.get(Objects.requireNonNull(TestComplexCommand.class.getClassLoader().getResource("com/oroarmor/json/brigadier/complex_command.json")).toURI())).build();

        assertTrue(CommandNodeEquals.equals(manualCommandNode, jsonCommandNode), "Parser correctly parses command from json");
    }

    @Test
    public void testRunCommand() throws URISyntaxException, CommandSyntaxException {
        LiteralArgumentBuilder<Object> manualCommandNode = literal("test")
                .requires(TestComplexCommand::requires)
                .then(literal("integer")
                        .then(argument("value", integer(0, 1))
                                .executes(TestComplexCommand::runCommandDouble))
                        .executes(TestComplexCommand::runCommandDoubleDefault))
                .then(literal("double")
                        .then(argument("value", doubleArg(0, 1))
                                .executes(TestComplexCommand::runCommandDouble))
                        .executes(TestComplexCommand::runCommandDoubleDefault));

        LiteralArgumentBuilder<Object> jsonCommandNode = (LiteralArgumentBuilder<Object>) JsonToBrigadier.parse(Paths.get(Objects.requireNonNull(TestComplexCommand.class.getClassLoader().getResource("com/oroarmor/json/brigadier/complex_command.json")).toURI()));


        CommandDispatcher<Object> dispatcher = new CommandDispatcher<>();
        dispatcher.register(manualCommandNode);
        dispatcher.execute("test integer 1", new Object());
        assertTrue(runSuccessful, "Manual command ran successfully");
        runSuccessful = false;

        dispatcher = new CommandDispatcher<>();
        dispatcher.register(jsonCommandNode);
        try {
            dispatcher.execute("test integer 1", new Object());
        } catch (CommandSyntaxException commandSyntaxException) {
            System.err.println(commandSyntaxException.getMessage());
        }
        assertTrue(runSuccessful, "Json command ran successfully");
    }

    @Test
    public void testParseToJson() {
        CommandNode<Object> manualCommandNode = literal("test")
                .then(literal("integer")
                        .then(argument("value", integer(0, 1))
                                .executes(TestComplexCommand::runCommandInteger))
                        .executes(TestComplexCommand::runCommandIntegerDefault))
                .then(literal("double")
                        .then(argument("value", doubleArg(0, 1))
                                .executes(TestComplexCommand::runCommandDouble))
                        .executes(TestComplexCommand::runCommandDoubleDefault))
                .build();

        assertTrue(CommandNodeEquals.equals(manualCommandNode, JsonToBrigadier.parse(BrigadierToJson.parse(manualCommandNode)).build()), "correct inverse parsing");
    }
}
