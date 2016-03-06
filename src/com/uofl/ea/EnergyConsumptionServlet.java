package com.uofl.ea;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uofl.ea.manager.ConsumptionManager;
import com.uofl.ea.manager.EventManager;
import com.uofl.ea.model.ConsumptionVO;
import com.uofl.ea.model.CurrentEventVO;

/**
 * This servlet returns the energy consumption for a user.
 */
@WebServlet("/EnergyConsumptionServlet")
public class EnergyConsumptionServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       

	/**
	 * This method gets the user name and responds with the energy consumed by the user and ON devices.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter("userName");
		if (userName == null || userName.isEmpty()) {
			response.getWriter().write("User Name is invalid.");
			response.getWriter().flush();
			response.getWriter().close();
			return;
		} else {
			List<CurrentEventVO> onEvents = EventManager.getEventManager().fetchOnEventsForUser(userName);
			List<ConsumptionVO> consumptions = ConsumptionManager.getConsumptionManager().fetchConsumptionByUser(userName);
			Double totalConsumption = 0.0;
			for (ConsumptionVO consumptionVO : consumptions) {
				if (consumptionVO.getEnergyConsumption() != null) {
					totalConsumption = totalConsumption + consumptionVO.getEnergyConsumption();
				}
			}
			
			request.setAttribute("userName", userName);
			request.setAttribute("onEvents", onEvents);
			request.setAttribute("consumptions", consumptions);
			request.setAttribute("totalConsumption", totalConsumption);
			
			RequestDispatcher rd= request.getRequestDispatcher("userRecord.jsp");
			rd.forward(request, response);
		}
	}



}
