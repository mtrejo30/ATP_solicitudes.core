package com.atp.solicitudes.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.atp.solicitudes.dao.SolicitudAppointmentDao;
import com.atp.solicitudes.model.Solicitud;
import com.atp.solicitudes.model.SolicitudAppointment;
import com.objectwave.dao.utils.DaoOrder;
import com.objectwave.dao.utils.DaoQuery;
import com.objectwave.dao.utils.DaoResult;
import com.objectwave.dao.utils.HibernateDaoTemplate;

public class SolicitudAppointmentDaoHibernate extends HibernateDaoTemplate implements SolicitudAppointmentDao
{
	private Criteria getCriteria()
	{
		return getSession().createCriteria(SolicitudAppointment.class);
	}
	
	public SolicitudAppointment getWithId(Integer id) throws Exception
	{
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("id", id));
		// get the collection
		@SuppressWarnings("unchecked")
		List<SolicitudAppointment> col = criteria.list();
		// if no elements found, return null
		// otherwise, return the first object found
		if (col.size() == 0)
			return null;
		else
			return col.get(0);
	}
	
	public SolicitudAppointment getWithAppNbr(String app_nbr, List<String> listid) throws Exception
	{
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("appointmentNbr", app_nbr));
		if (listid != null){
		criteria.add(Restrictions.in("solicitud.folio", listid));
		}
		// get the collection
		@SuppressWarnings("unchecked")
		List<SolicitudAppointment> col = criteria.list();

		// if no elements found, return null
		// otherwise, return the first object found
		if (col.size() == 0)
			return null;
		else
			return col.get(0);
	}
	
	
	 public List<String> getSolicitudBy_(String user) throws Exception
		{
 	Query query = getSession().createQuery("select model.folio from Solicitud as model where user.id = "+ user);
@SuppressWarnings("unchecked")
			List<String> queryResult = (List<String>)query.list();
			return queryResult;		
		}
	
	public List <SolicitudAppointment> getWithSolicitud(Solicitud solicitud) throws Exception
	{
		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("solicitud", solicitud));

		// get the collection
		@SuppressWarnings("unchecked")
		List<SolicitudAppointment> col = criteria.list();

		// if no elements found, return null
		// otherwise, return the list of objects
		if (col.size() == 0)
			return null;
		else
			return col;
	}
	
	public void save(SolicitudAppointment object) throws Exception
	{
		getHibernateTemplate().saveOrUpdate(object);
	}

	public DaoResult<SolicitudAppointment> query(DaoQuery query, DaoOrder order)
	{
		Criteria criteria = getCriteria();

		query.applyFetchingToCriteria(criteria);

		String[] eqs = {"id", "solicitud", "status"};
		registerEqPaths(eqs, query, criteria);
		
		if (query.hasFilter("status_in"))
		{
			criteria.add(Restrictions.in("status", (Object[]) query.get("status_in")));
		}

		DaoResult<SolicitudAppointment> result = new DaoResult<SolicitudAppointment>();

		result.processWith(query, order, criteria);

		// return the result
		return result;
	}
	
	public DaoResult<SolicitudAppointment> query(DaoQuery query, DaoOrder order, Integer idTransporter)
	{
		Criteria criteria = getCriteria();
		query.applyFetchingToCriteria(criteria);
		String[] eqs = {"id", "solicitud", "status"};
		registerEqPaths(eqs, query, criteria);
		
		if (query.hasFilter("status_in"))
		{
			criteria.add(Restrictions.in("status", (Object[]) query.get("status_in")));
		}
		
		criteria.add(Restrictions.eq("transportistaId", idTransporter));

		DaoResult<SolicitudAppointment> result = new DaoResult<SolicitudAppointment>();

		result.processWith(query, order, criteria);

		// return the result
		return result;
	}
	
	
	public List<String> getColumnValues(String column, String match, int maxResults) throws Exception
	{
		Query query = getSession().createQuery("select distinct " + column + " from SolicitudAppointment where " + column + " like ? order by " + column + " asc");
		query.setString(0, match);
		if(maxResults != 0)
			query.setMaxResults(maxResults);
		@SuppressWarnings("unchecked")
		List<String> queryResult = query.list();
		
		return queryResult;
	}
	
	public SolicitudAppointment getSolicitudIdd(String column, String match) throws Exception
	{
		Query query = getSession().createQuery("select * from SolicitudAppointment where " + column + "=" +match);
     	query.setString(0, match);
		
//		if(maxResults != 0)
//			query.setMaxResults(maxResults);
//		
//		@SuppressWarnings("unchecked")
		SolicitudAppointment queryR = (SolicitudAppointment) query;
//		
		return queryR;
	}
	
	
	public Integer getSolicitudId(String app_nbr) throws Exception
	{
//		Query query = session.createQuery(hql);
		Query query = getSession().createQuery("select solicitud from SolicitudAppointment where appoitnment_nbr=" +app_nbr);
//		query.setString(0, app_nbr);
		List<String> queryResult = query.list();
		String aux=query.toString();
		Integer aux2=Integer.parseInt(aux);
		return aux2;
	}

	public List<String> getSolicitudesByUserN4(Integer userN4) throws Exception{
		
		Query query = getSession().createQuery("select distinct solicitud_id from SolicitudAppointment where transportista_id in (select DISTINCT transportista_id from UserTransportistas where user_n4_id = "+userN4+")");
		
		@SuppressWarnings("unchecked")
		List<String> queryResult = query.list();
		
		return queryResult;
	}
}
