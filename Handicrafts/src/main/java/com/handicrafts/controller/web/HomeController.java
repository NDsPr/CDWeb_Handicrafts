package com.handicrafts.controller.web;

import com.ltw.bean.*;
import com.ltw.dao.BlogDAO;
import com.ltw.dao.CategoryDAO;
import com.ltw.dao.CustomizeDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// TODO: Xem lại cách chèn link
@WebServlet(value = {"/home"})
public class HomeController extends HttpServlet {
    private final CustomizeDAO customizeDAO = new CustomizeDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final BlogDAO blogDAO = new BlogDAO();

    // TODO: Cần thêm thông báo cho người dùng khi cố ý truy cập vào trang admin khi role là client
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CustomizeBean customizeInfo = customizeDAO.getCustomizeInfo();
        String prIcon1 = customizeInfo.getPrIcon1();
        String prContentTitle1 = customizeInfo.getPrContentTitle1();
        String prContentDes1 = customizeInfo.getPrContentDes1();
        String prContent2 = customizeInfo.getPrContent2();

        // Xử lý các thành phần phân tách bằng dấu ("~" hoặc ",") để lấy ra mảng content
        // Đã xử lý trước khi lưu vào database, nhưng vẫn nên xử lý lại
        // Lưu ý là số lượng icon = số lương title = số lượng nội dung (Đã xử lý trong admin)
        String[] prIcon1List = splitByComma(prIcon1);
        String[] prContentTitle1List = splitByTilde(prContentTitle1);
        String[] prContentDes1List = splitByTilde(prContentDes1);
        List<Content1Bean> listContent1 = putContentIntoList(prIcon1List, prContentTitle1List, prContentDes1List);

        String[] prContent2List = splitByTilde(prContent2);

        List<CategoryBean> listCategories = categoryDAO.findAllCategories();
        List<BlogBean> listThreeBlogs = blogDAO.findThreeBlogs();

        req.setAttribute("customizeInfo", customizeInfo);
        req.setAttribute("listCategories", listCategories);
        req.setAttribute("listBlogs", listThreeBlogs);
        req.setAttribute("listContent1", listContent1);
        req.setAttribute("prIcon1List", prIcon1List);
        req.setAttribute("prContent2List", prContent2List);

        req.getRequestDispatcher("/client-home.jsp").forward(req, resp);
    }

    // Hàm lấy ra danh sách văn bản phân cách bằng dấu "~" (Content)
    private String[] splitByTilde(String input) {
        // Trước tiên cần loại bỏ khoảng trắng ở đầu và cuối chuỗi
        String inputClearSpace = clearSpaceHeaderAndFooter(input);
        return inputClearSpace.split("~\\s*");
    }

    // Hàm lấy ra danh sách văn bản phân cách bằng dấu "," (Icon)
    private String[] splitByComma(String input) {
        // Trước tiên cần loại bỏ khoảng trắng ở đầu và cuối chuỗi
        String inputClearSpace = clearSpaceHeaderAndFooter(input);
        return inputClearSpace.split(",\\s*");
    }

    private String clearSpaceHeaderAndFooter(String input) {
        return input.replace("^\\s+|\\s+$", "");
    }

    private List<Content1Bean> putContentIntoList(String[] iconArray, String[] contentTitleArray, String[] contentDesArray) {
        List<Content1Bean> content1List = new ArrayList<>();
        // 3 array phải có length bằng nhau (Đã xử lý khi thêm trong admin)
        for (int i = 0; i < iconArray.length; i++) {
            Content1Bean content1Bean = new Content1Bean();
            content1Bean.setPrIcon1(iconArray[i]);
            content1Bean.setPrContentTitle1(contentTitleArray[i]);
            content1Bean.setPrContentDes1(contentDesArray[i]);

            content1List.add(content1Bean);
        }
        return content1List;
    }
}
