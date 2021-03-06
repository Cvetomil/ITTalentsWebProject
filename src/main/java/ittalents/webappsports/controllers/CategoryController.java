package ittalents.webappsports.controllers;

import ittalents.webappsports.dao.CategoryDAO;
import ittalents.webappsports.dto.CategoryReadsDTO;
import ittalents.webappsports.exceptions.BadRequestException;
import ittalents.webappsports.models.Category;
import ittalents.webappsports.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    CategoryRepository cr;

    //get category from database
    @GetMapping("/categories/{id}")
    public Category getCategory(@PathVariable long id) throws BadRequestException {
        if (!cr.findById(id).isPresent()) {
            throw new BadRequestException("No such category");
        }
        return cr.findById(id).get();
    }

    //gets all categories by title
    @GetMapping("/categories/search/{title}")
    public List<Category> findByName(@PathVariable String title) {
        return cr.getAllByNameContaining(title);
    }

    @GetMapping("/categories/totalreads")
    public List<CategoryReadsDTO> categoriesByTotalReads (){
        return categoryDAO.getCategoriesByTotalReads();
    }
}
