package controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Task;
import model.validators.MessageValidator;
import utils.DBUtil;

/**
 * Servlet implementation class CreateServlet
 */
@WebServlet("/create")
public class CreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*トークンを受け取る*/
        String _token = (String)request.getParameter("_token");
        // もしとーくんが空でなくて、今取得したセッションIDと一致した場合、
        if(_token != null && _token.equals(request.getSession().getId())) {
            // EntityManagerが作成されデータベースとのやり取りが始まる
            EntityManager em = DBUtil.createEntityManager();
            Task t = new Task();
            String content = request.getParameter("content");
            t.setContent(content);

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            t.setCreated_at(currentTime);
            t.setUpdated_at(currentTime);

            // validationを実行してエラーがあったら新規登録のフォームに戻る
            List<String> errors = MessageValidator.validate(t);
            if(errors.size() > 0) {
                em.close();

                // フォームに初期値を設定、さらにエラーメッセージを送る
                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("task", t);
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/tasks/new.jsp");
                rd.forward(request, response);
            } else {


            // データベースの書き換えが行われる場合は必ずgetTransaction().begin()で始まる
            em.getTransaction().begin();
            // 新規追加の時h￥はpersistメソッドを使用して追加する　https://www.tuyano.com/index3?id=9736003&page=2
            em.persist(t);
            // 最後にコミットすることで保存される
            em.getTransaction().commit();
            request.getSession().setAttribute("flush", "登録が完了しました。");
            em.close();

            response.sendRedirect(request.getContextPath() + "/index");
            }
        }

    }

}
