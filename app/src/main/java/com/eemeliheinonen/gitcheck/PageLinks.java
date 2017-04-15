package com.eemeliheinonen.gitcheck;

import com.eemeliheinonen.gitcheck.models.Repository;

import java.util.List;

import retrofit2.Response;

/**
 * Created by eemeliheinonen on 12/04/2017.
 */

public class PageLinks {

    private String first;
    private String last;
    private String next;
    private String prev;

    /**
     * Parse links from executed method
     *
     * @param response
     */

    public PageLinks(){
        //Required empty constructor
    }

    public void setLinks(Response<List<Repository>> response) {
        String linkHeader = response.headers().get("Link");
        if (linkHeader != null) {
            String[] links = linkHeader.split(",");
            for (String link : links) {
                String[] segments = link.split(";");
                if (segments.length < 2)
                    continue;

                String linkPart = segments[0].trim();
                if (!linkPart.startsWith("<") || !linkPart.endsWith(">")) //$NON-NLS-1$ //$NON-NLS-2$
                    continue;
                linkPart = linkPart.substring(1, linkPart.length() - 1);

                for (int i = 1; i < segments.length; i++) {
                    String[] rel = segments[i].trim().split("="); //$NON-NLS-1$
                    if (rel.length < 2 || !rel[0].equals("rel"))
                        continue;

                    String relValue = rel[1];
                    if (relValue.startsWith("\"") && relValue.endsWith("\"")) //$NON-NLS-1$ //$NON-NLS-2$
                        relValue = relValue.substring(1, relValue.length() - 1);

                    switch (relValue) {
                        case "first":
                            first = linkPart;
                            break;
                        case "last":
                            last = linkPart;
                            break;
                        case "next":
                            next = linkPart;
                            break;
                        case "prev":
                            prev = linkPart;
                            break;
                    }
                }
            }
        } else {
            next = response.headers().get("X-Next");
            last = response.headers().get("X-Last");
        }
    }

    /**
     * @return first
     */
    public String getFirst() {
        return first;
    }

    /**
     * @return last
     */
    public String getLast() {
        return last;
    }

    /**
     * @return next
     */
    public String getNext() {
        return next;
    }

    /**
     * @return prev
     */
    public String getPrev() {
        return prev;
    }
}
