package com.example.controller;

import com.example.exception.BusinessException;
import com.example.model.Show;
import com.example.exception.MyException;
import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.DgsQuery;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@DgsComponent
public class ShowsDatafetcher {

    private final List<Show> shows = Arrays.asList(
            new Show(1, "Stranger Things", 2016),
            new Show(2, "Ozark", 2017),
            new Show(3, "The Crown", 2016),
            new Show(4, "Dead to Me", 2019),
            new Show(5, "Orange is the New Black", 2013)
    );

    @DgsQuery
    public List<Show> getShows(@InputArgument String titleFilter) {
        if(titleFilter == null) {
            return shows;
        }
        return shows.stream().filter(s -> s.getTitle().contains(titleFilter)).collect(Collectors.toList());
    }

    @DgsQuery
    public List<Show> getShowsById(@InputArgument Integer id) {
        if(id == null) {
            return shows;
        }
        List<Show> showsReturned = shows.stream().filter(s -> s.getId().equals(id)).collect(Collectors.toList());
        log.info("Shows returned: {} for ID: {}", showsReturned, id);
        return showsReturned;
    }

    @DgsQuery
    public String getMyException(){
        throw new MyException("Custom error raised!");
    }

    @DgsQuery
    public String getBusinessException(){
        throw new BusinessException("Business exception raised!");
    }

    @DgsQuery(field = "uploadFile")
    public UUID uploadFile(DataFetchingEnvironment dfe) throws IOException {
        System.out.println("Passei aqui...");
        MultipartFile file = dfe.getArgument("file");
        String content = new String(file.getBytes());
        System.out.println(content);
        return UUID.randomUUID();
    }

//    @DgsQuery(field = "uploadFile")
//    public boolean uploadFile(DataFetchingEnvironment dfe) throws IOException {
//        // NOTE: Cannot use @InputArgument  or Object Mapper to convert to class, because MultipartFile cannot be deserialized
//        MultipartFile file = dfe.getArgument("input");
//        String content = new String(file.getBytes());
//        return ! content.isEmpty();
//    }

//    @DgsData(parentType = "Mutation")
//    public Mono<String> uploadFile(DataFetchingEnvironment env) {
//        FileDataFetcher.log.info("Received a request to upload a file");
//    }

//    @DgsMutation
//    public List<Image> addArtwork(@InputArgument Integer showId, @InputArgument MultipartFile upload) throws IOException {
//        Path uploadDir = Paths.get("uploaded-images");
//        if (!Files.exists(uploadDir)) {
//            Files.createDirectories(uploadDir);
//        }
//
//        Path newFile = uploadDir.resolve("show-" + showId + "-" + UUID.randomUUID() + upload.getOriginalFilename().substring(upload.getOriginalFilename().lastIndexOf(".")));
//        try (OutputStream outputStream = Files.newOutputStream(newFile)) {
//            outputStream.write(upload.getBytes());
//        }
//
//        return Files.list(uploadDir)
//                .filter(f -> f.getFileName().toString().startsWith("show-" + showId))
//                .map(f -> f.getFileName().toString())
//                .map(fileName -> Image.newBuilder().url(fileName).build()).collect(Collectors.toList());
//
//    }
}
