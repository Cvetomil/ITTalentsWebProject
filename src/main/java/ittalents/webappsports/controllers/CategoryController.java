package ittalents.webappsports.controllers;

import ittalents.webappsports.models.Category;
import ittalents.webappsports.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CategoryRepository cr;

    @GetMapping("/categories/{id}")
public Category getCategory (@PathVariable long id){
    return cr.findById(id).get();
}

@GetMapping("/categories/search/{title}")
public List<Category> findByName (@PathVariable String title){


        return cr.getAllByNameContaining(title);
}
}
