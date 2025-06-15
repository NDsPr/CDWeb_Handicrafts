package com.handicrafts.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handicrafts.dao.ProductDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = {"/api/suggest-key"})
public class SuggestionAPI extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String key = req.getParameter("key");
        ObjectMapper objectMapper = new ObjectMapper();
        String stringJSON;
        if (key != null) {
            if (!key.isEmpty()) {
                List<String> suggestKeys = productDAO.getSuggestTitle(key);
                // Chuyển từ Object sang String JSON
                stringJSON = objectMapper.writeValueAsString(suggestKeys);
            } else {
                // Trường hợp key rỗng, thực hiện hành động tương ứng
                stringJSON = objectMapper.writeValueAsString("");
            }
        } else {
            // Trường hợp key rỗng, thực hiện hành động tương ứng
            stringJSON = objectMapper.writeValueAsString("");
        }

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().write(stringJSON);
    }
}
