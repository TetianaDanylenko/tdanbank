package de.oth.tdanylenko.tdanbank.service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

public interface BusinessProductServiceIF {
    Boolean postBusinessProductToTheMarketPlace (long id, RedirectAttributes redirect) throws IOException;
    Boolean deleteBusinessProductDTOfromMarketPlace (long id);
    String getStatsOfBusinessProduct (long id);
}
