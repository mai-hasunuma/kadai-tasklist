package controllers;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Task;
import utils.DBUtil;

/**
 * Servlet implementation class IndexServlet
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // DBからデータを取得するためにemインスタンス作成
        EntityManager em = DBUtil.createEntityManager();
        // createNamedQuery() ()ないのクエリを実行する
        List<Task> tasks = em.createNamedQuery("getAllTasks", Task.class)
                .getResultList();
        // データをしゅとくできたので終了
        em.close();

        // 先ほど取得したやつをリクエストスコープに格納
        request.setAttribute("tasks", tasks);

        // フラッシュメッセージがセッションスコープにセットされていたら
        // リクエストスコープに保存する

        if (request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/tasks/index.jsp");
        rd.forward(request, response);
    }

}
