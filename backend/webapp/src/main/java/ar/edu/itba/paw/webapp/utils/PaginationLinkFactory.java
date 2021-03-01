package ar.edu.itba.paw.webapp.utils;


import org.springframework.stereotype.Component;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.Map;

@Component
public class PaginationLinkFactory {

    private static final String PAGE = "page";

    public static final String FIRST = "first";
    public static final String NEXT = "next";
    public static final String PREV = "prev";
    public static final String LAST = "last";

    public Map<String, Link> createLinks(final UriInfo uriRequestInfo, final int requestPage, final int maxPage) {
        final Map<String, Link> map = new HashMap<>();
        final UriBuilder requestBuilder = uriRequestInfo.getRequestUriBuilder();

        if (requestPage < maxPage)
            map.put(NEXT, linkWithRel(setPageParam(requestBuilder, requestPage < 1 ? 1 : requestPage + 1), NEXT));

        if (requestPage > 1)
            map.put(PREV, linkWithRel(setPageParam(requestBuilder, requestPage > maxPage ? maxPage : requestPage - 1), PREV));

        map.put(FIRST, linkWithRel(setPageParam(requestBuilder, 1), FIRST));
        map.put(LAST, linkWithRel(setPageParam(requestBuilder, maxPage), LAST));

        return map;
    }

    private Link linkWithRel(final UriBuilder requestBuilder, final String rel) {
        return Link.fromUriBuilder(requestBuilder).rel(rel).build();
    }

    private UriBuilder setPageParam(final UriBuilder requestBuilder, final int page) {
        return requestBuilder.replaceQueryParam(PAGE, page);
    }
}
