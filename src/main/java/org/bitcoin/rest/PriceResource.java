package org.bitcoin.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bitcoin.bll.BusinessLogicLayer;
import org.bitcoin.bll.model.Price;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Path("/price")
@SuppressWarnings("unused")
public class PriceResource {
    private static final Logger LOGGER = Logger.getLogger(PriceResource.class.getName());

    private final BusinessLogicLayer bll;

    public PriceResource(BusinessLogicLayer bll) {
        this.bll = bll;
    }

    @GET
    @Path("{dateStr}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrice(@PathParam("dateStr") String dateStr) {
        LOGGER.info("GET request for price: " + dateStr);
        Optional<Price> price = bll.getPrice(dateStr);

        if (price.isPresent()) {
            return Response.ok()
                    .entity(price.get())
                    .build();
        } else {
            return Response.noContent().build();
        }
    }

    @GET
    @Path("{startDateStr}/{endDateStr}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPriceRange(@PathParam("startDateStr") String startDateStr, @PathParam("endDateStr") String endDateStr) throws ParseException {
        LOGGER.info(String.format("GET request for price range: %s - %s", startDateStr, endDateStr));
        List<Price> prices = bll.getPriceRange(startDateStr, endDateStr);

        return Response.ok()
                .entity(prices)
                .build();
    }
}