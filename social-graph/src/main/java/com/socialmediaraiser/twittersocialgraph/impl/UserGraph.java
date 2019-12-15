package com.socialmediaraiser.twittersocialgraph.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;

@Data
@AllArgsConstructor
@JsonSerialize(using = UserGraph.GroupSerializer.class)
public class UserGraph{
    private String id;
    private GroupEnum groupEnum;
    private int size;

    public UserGraph(String id, GroupEnum groupEnum){
        this.id = id;
        this.groupEnum = groupEnum;
        this.size = 5;
    }

    public static class GroupSerializer extends JsonSerializer<UserGraph> {

        @Override
        public void serialize(
                UserGraph userGraph, JsonGenerator jgen, SerializerProvider provider)
                throws IOException {
            jgen.writeStartObject();
            jgen.writeStringField("id", userGraph.getId());
            jgen.writeNumberField("group", userGraph.getGroupEnum().getGroupId());
            jgen.writeNumberField("size", userGraph.getSize());
            jgen.writeEndObject();
        }

    }
}


