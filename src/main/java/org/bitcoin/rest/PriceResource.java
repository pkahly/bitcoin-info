package org.bitcoin.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bitcoin.bll.IBusinessLogicLayer;
import org.bitcoin.bll.PriceRangeType;
import org.bitcoin.bll.model.Price;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Path("/price")
@SuppressWarnings("unused")
public class PriceResource {
    private static final Logger LOGGER = Logger.getLogger(PriceResource.class.getName());

    private final IBusinessLogicLayer bll;

    public PriceResource(IBusinessLogicLayer bll) {
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
    public Response getPriceRange(@PathParam("startDateStr") String startDateStr, @PathParam("endDateStr") String endDateStr, @QueryParam("rangeType") PriceRangeType rangeType) throws ParseException {
        if (rangeType == null) {
            rangeType = PriceRangeType.DAY;
        }

        LOGGER.info(String.format("GET request for price range: %s - %s", startDateStr, endDateStr));
        LOGGER.info("Price Range Type: " + rangeType);

        List<Price> prices = bll.getPriceRange(startDateStr, endDateStr, rangeType);

        return Response.ok()
                .entity(prices)
                .build();
    }
}