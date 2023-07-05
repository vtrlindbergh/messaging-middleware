package br.imd.messagemanager.model;

import com.fasterxml.jackson.databind.JsonNode;

public record MiddlewareMessage(String context, JsonNode body) {}
