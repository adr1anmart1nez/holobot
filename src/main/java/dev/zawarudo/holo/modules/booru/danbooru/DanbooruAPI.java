package dev.zawarudo.holo.modules.booru.danbooru;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import dev.zawarudo.holo.modules.booru.BooruAPI;
import dev.zawarudo.holo.utils.exceptions.APIException;
import dev.zawarudo.holo.utils.exceptions.InvalidRequestException;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DanbooruAPI extends BooruAPI<DanbooruPost> {

    public static final int MAX_LIMIT = 200;
    public static final String BASE_URL = "https://danbooru.donmai.us";

    private static final Logger LOGGER = LoggerFactory.getLogger(DanbooruAPI.class);

    /**
     * Returns the number of posts that have the given set of tags.
     *
     * @param tags The tags the posts should have. For the total count of Danbooru posts, simply give no tag.
     * @return The count of posts with those tags.
     */
    public static int getPostCount(String... tags) throws APIException, InvalidRequestException {
        String url = String.format("%s/counts/posts.json", BASE_URL);
        if (tags.length > 0) {
            url += "?tags=" + String.join("+", tags);
        }
        String json = getJsonString(url);
        JsonObject object = new Gson().fromJson(json, JsonObject.class);
        return object.getAsJsonObject("counts").get("posts").getAsInt();
    }

    @Override
    public List<DanbooruPost> getPosts() throws InvalidRequestException, APIException {
        String url = prepareUrl();
        String json = getJsonString(url);
        Type type = new TypeToken<List<DanbooruPost>>() {}.getType();
        return Collections.unmodifiableList(new Gson().fromJson(json, type));
    }

    @Override
    public List<DanbooruPost> getAllPosts() throws APIException, InvalidRequestException {
        if (tags.isEmpty()) {
            throw new IllegalArgumentException("No tags given! Requires at least one tag!");
        }
        List<DanbooruPost> result = new ArrayList<>();
        String url = prepareUrlPagination();
        int page = 1;
        List<DanbooruPost> posts;
        do {
            try {
                String json = getJsonString(url + "&page=" + page);
                Type type = new TypeToken<List<DanbooruPost>>() {
                }.getType();
                posts = new Gson().fromJson(json, type);
                result.addAll(posts);
                page++;
            } catch (APIException e) {
                return Collections.unmodifiableList(result);
            }
        } while (!posts.isEmpty());
        return Collections.unmodifiableList(result);
    }

    @Override
    @Deprecated
    public List<DanbooruPost> getAllPosts(int page) throws APIException, InvalidRequestException {
        String url;
        if (tags.isEmpty()) {
            url = String.format("%s/posts.json?limit=%d&page=%d", BASE_URL, MAX_LIMIT, page);
        } else {
            url = String.format("%s&page=%d", prepareUrlPagination(), page);
        }
        String json = getJsonString(url);
        Type type = new TypeToken<List<DanbooruPost>>() {}.getType();
        return new Gson().fromJson(json, type);
    }

    private String prepareUrl() {
        String tagsString = tags.isEmpty() ? "" : "+" + String.join("+", this.tags);
        String blacklistedString = blacklisted.isEmpty() ? "" : "-" + String.join("-", this.blacklisted);
        String rating = this.rating.getValue();
        String order = this.order.getValue().replace("N", String.valueOf(limit));
        return String.format("%s/posts.json?limit=%d&tags=%s+%s%s%s", BASE_URL, limit, rating, order, tagsString, blacklistedString);
    }

    private String prepareUrlPagination() {
        String tagsString = String.join("+", this.tags);
        return String.format("%s/posts.json?limit=%d&tags=%s", BASE_URL, MAX_LIMIT, tagsString);
    }

    public static DanbooruPost getPost(int id) throws APIException, InvalidRequestException {
        String url = String.format("%s/posts/%d.json", BASE_URL, id);
        String json = getJsonString(url);
        return new Gson().fromJson(json, DanbooruPost.class);
    }

    private static String getJsonString(String url) throws APIException, InvalidRequestException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sending a request to {}...", url);
        }
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = sendRequest(request);
            checkResponseCode(response);
            return getBody(response);
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("An error occurred while sending a request and fetching the response:", e);
            }
            throw new APIException("Something unexpected happened! (" + e.getCause() + ")");
        }
    }
}
