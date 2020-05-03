package com.maqs.rdbmsevaluation.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class EntityUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static CsvMapper mapper = new CsvMapper();

    static {
        mapper.enable(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE);
    }

    public static <E> List<E> readJsonFile(String file, Class<E> clazz) {
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(
                    EntityUtil.class.getResource(file).toURI()));
            CollectionType javaType = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, clazz);
            List<E> list = objectMapper.readValue(fileBytes, javaType);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <E> List<E> readCsvFile(String filePath, Class<E> clazz, char separator) {
        File file = new File(EntityUtil.class.getResource(filePath).getFile());
        return readCsvFile(file, clazz, separator);
    }

    public static <E> List<E> readCsvFile(File file, Class<E> clazz, char separator, CsvSchema schema) {
        List<E> list = null;
        try {
            log.debug("creating records of type: " + clazz.getSimpleName() + " from file " + file.getAbsolutePath());
            MappingIterator<E> iterator = mapper
                    .readerFor(clazz)
                    .with(schema).readValues(file);

            list = iterator.readAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    public static <E> List<E> readCsvFile(File file, Class<E> clazz, char separator) {
        long start = System.currentTimeMillis();
        CsvMapper mapper = new CsvMapper();
        mapper.enable(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE);

        CsvSchema schema = mapper.typedSchemaFor(clazz)
                .withSkipFirstDataRow(true)
                .withColumnSeparator(separator);

        List<E> list = readCsvFile(file, clazz, separator, schema);
        Util.printTimeTaken(start, "Read " + file.getPath());
        return list;
    }
}
