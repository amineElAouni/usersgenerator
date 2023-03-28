package com.cires.usersgenerator.common;

import com.cires.usersgenerator.dto.UserDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class JsonExporterImpl implements JsonExporter{

    @Override
    public String export(List<UserDto> users) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .disableHtmlEscaping()
                .create();
        return gson.toJson(users).replace("\\\\", "\\");
    }
}
