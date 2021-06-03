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

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

public class TestSimpleCommand {
    private static int runCommand(CommandContext<Object> o) {
        System.out.println("Test Command");
        return 1;
    }

    @Test
    public void testParse() throws URISyntaxException {
        CommandNode<Object> manualCommandNode = literal("test")
                .then(argument("value", integer(0, 1))
                        .executes(TestSimpleCommand::runCommand))
                .build();

        CommandNode<Object> jsonCommandNode = JsonToBrigadier.parse(Paths.get(Objects.requireNonNull(TestSimpleCommand.class.getClassLoader().getResource("com/oroarmor/json/brigadier/test_command.json")).toURI())).build();

        Assertions.assertTrue(CommandNodeEquals.equals(manualCommandNode, jsonCommandNode), "Parser correctly parses command from json");
    }
}
