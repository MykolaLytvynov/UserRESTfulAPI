package ua.mykola.UserRESTfulAPI.rest.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * This class is used for entity response to communicate error
 * information in the application.
 */
@Builder
@Getter
@Setter
public class ErrorMessage {
    private int status;
    private String message;
}
