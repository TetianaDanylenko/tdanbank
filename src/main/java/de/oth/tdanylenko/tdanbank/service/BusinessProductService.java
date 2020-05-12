package de.oth.tdanylenko.tdanbank.service;

import de.oth.tdanylenko.tdanbank.dto.BusinessProductDTO;
import de.oth.tdanylenko.tdanbank.entity.BusinessProducts;
import de.oth.tdanylenko.tdanbank.enums.ProductType;
import de.oth.tdanylenko.tdanbank.exceptions.BusinessProductException;
import de.oth.tdanylenko.tdanbank.exceptions.PartnerServiceIsNotAvailableException;
import de.oth.tdanylenko.tdanbank.repository.BusinessProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.util.Arrays;

@Service
@Scope("singleton")
public class BusinessProductService implements BusinessProductServiceIF{
    @Autowired
    private final RestTemplate restClient;
    @Autowired
    private final BusinessProductRepository productRepo;

    public BusinessProductService(RestTemplate restClient, BusinessProductRepository productRepo) {
        this.restClient = restClient;
        this.productRepo = productRepo;
    }

    public BusinessProductDTO toDto(BusinessProducts product) throws IOException {
        BusinessProductDTO dto = new BusinessProductDTO();
        dto.setId(product.getId());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setName(product.getName());
        dto.setCategory(Arrays.asList(product.getType().toString()));
        dto.setImage(this.getImageAsByte(ProductType.CREDITCARD));
        return dto;
    }
    private byte[] getImageAsByte(ProductType type) throws IOException {
        Boolean isCreditCard = false;
        if(type.equals(ProductType.CREDITCARD)){
            isCreditCard = true;
        } else {
            isCreditCard = false;
        }
        File file = isCreditCard ? new File("src/main/resources/image/dummycreditcard2.jpg") : new File("src/main/resources/image/dummyinsurance.jpg");
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                //Writes to this byte array output stream
                bos.write(buf, 0, readNum);
                System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
        }
        byte[] bytes = bos.toByteArray();
        return bytes;
    }

    @Override
    public Boolean postBusinessProductToTheMarketPlace(long id, RedirectAttributes redirect) throws PartnerServiceIsNotAvailableException, IOException {
         BusinessProducts product = productRepo.getBusinessProductsById(id);
            BusinessProductDTO productDTO = this.toDto(product);
            try {
                ResponseEntity<BusinessProductDTO> response = restClient.postForEntity("http://im-codd:8801/restapi/product/" + id +"/add", productDTO, BusinessProductDTO.class);
            } catch (Exception e) {
                redirect.addAttribute("serviceUnavailable", true);
                redirect.addFlashAttribute("serviceIsUnavailable", "partner service temporary unavailable");
                return false;
            }
            redirect.addAttribute("serviceAnswered", true);
            redirect.addFlashAttribute("serviceResponded", "product was added to the market place");
            return true;
    }

    @Override
    public Boolean deleteBusinessProductDTOfromMarketPlace(long id) throws PartnerServiceIsNotAvailableException{
        BusinessProducts product = productRepo.getBusinessProductsById(id);
        try {
            ResponseEntity<Boolean> response = restClient.postForEntity("http://im-codd:8801/restapi/product/" + id +"/delete", product, Boolean.class);
        } catch (PartnerServiceIsNotAvailableException e) {
            return false;
        }
        return true;
    }

    @Override
    public String getStatsOfBusinessProduct(long id) throws PartnerServiceIsNotAvailableException {
        BusinessProducts product = productRepo.getBusinessProductsById(id);
        ResponseEntity<String> response = restClient.postForEntity("http://im-codd:8801/restapi/product/" + id +"/info", product, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BusinessProductException("Information is temporary unavailable");
        }
        return response.getBody();
    }
}
