package com.atp.solicitudes.manager.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.codehaus.jackson.JsonNode;
import org.omg.CORBA.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.atp.solicitudes.constants.ActivityLogEntryConstants;
import com.atp.solicitudes.dao.AgenciaAduanalDao;
import com.atp.solicitudes.dao.BookingDao;
import com.atp.solicitudes.dao.BookingItemDao;
import com.atp.solicitudes.dao.ClienteDao;
import com.atp.solicitudes.dao.ContenedorDao;
import com.atp.solicitudes.dao.LineaNavieraDao;
import com.atp.solicitudes.dao.OperadorTransportistaDao;
import com.atp.solicitudes.dao.PaqueteComercialDao;
import com.atp.solicitudes.dao.RecintoOrigenDao;
import com.atp.solicitudes.dao.SpecialDao;
import com.atp.solicitudes.dao.StoredProceduresDao;
import com.atp.solicitudes.dao.TipoRecepcionVacioDao;
import com.atp.solicitudes.dao.TransportistaDao;
import com.atp.solicitudes.dao.UserDao;
import com.atp.solicitudes.dao.UserN4Dao;
import com.atp.solicitudes.manager.DomainManager;
import com.atp.solicitudes.manager.DomainManagerN4;
import com.atp.solicitudes.model.AgenciaAduanal;
import com.atp.solicitudes.model.Booking;
import com.atp.solicitudes.model.BookingItem;
import com.atp.solicitudes.model.Cliente;
import com.atp.solicitudes.model.Contenedor;
import com.atp.solicitudes.model.LineaNaviera;
import com.atp.solicitudes.model.OperadorTransportista;
import com.atp.solicitudes.model.PaqueteComercial;
import com.atp.solicitudes.model.RecintoOrigen;
import com.atp.solicitudes.model.Solicitud;
import com.atp.solicitudes.model.SolicitudAppointment;
import com.atp.solicitudes.model.SolicitudAppointmentStatusEnum;
import com.atp.solicitudes.model.Special;
import com.atp.solicitudes.model.TipoRecepcionVacio;
import com.atp.solicitudes.model.Transportista;
import com.atp.solicitudes.model.User;
import com.atp.solicitudes.model.UserN4;
import com.atp.solicitudes.model.ContenedorStatus;
import com.atp.solicitudes.reports.model.ArrivalTimeContenedor;
import com.atp.solicitudes.webservice.client.N4WebServiceTemplate;
import com.navis.argo.AppointmentListRequestType;
import com.navis.argo.AppointmentRequestBaseType;
import com.navis.argo.ContainerType;
import com.navis.argo.DriverBaseRequestType;
import com.navis.argo.GateAppointmentRequestType;
import com.navis.argo.GateWebserviceRequestType;
import com.navis.argo.GateWebserviceResponseType;
import com.navis.argo.Hpu;
import com.navis.argo.Icu;
import com.navis.argo.SlotType;
import com.navis.argo.TFlags;
import com.navis.argo.THpu;
import com.navis.argo.TIcu;
import com.navis.argo.TUpdateFlags;
import com.navis.argo.TruckBaseRequestType;
import com.navis.argo.webservice.types.v1.MessageType;
import com.navis.argo.webservice.types.v1.ResponseType;
import com.navis.services.argoservice.GenericInvokeResponse;
import com.objectwave.dao.utils.DaoOrder;
import com.objectwave.dao.utils.DaoQuery;
import com.objectwave.dao.utils.DaoResult;
import com.objectwave.exception.DomainModelException;
import com.objectwave.logger.ActivityEvent;
import com.objectwave.model.ActivityLogEntry;
import com.objectwave.utils.AppContextProvider;
import com.objectwave.utils.SimpleEntry;

@Transactional(readOnly = true)
public class DomainManagerN4Impl extends AppContextProvider implements DomainManagerN4
{
	protected static Logger logger = LoggerFactory.getLogger(DomainManagerN4Impl.class);

	@Resource(name="agenciaAduanalDao")
	private AgenciaAduanalDao agenciaAduanalDao;

	@Resource(name="lineaNavieraDao")
	private LineaNavieraDao lineaNavieraDao;
	
	@Resource(name="clienteDao")
	private ClienteDao clienteDao;

	@Resource(name="operadorTransportistaDao")
	private OperadorTransportistaDao operadorTransportistaDao;

	@Resource(name="transportistaDao")
	private TransportistaDao transportistaDao;

	@Resource(name="contenedorDao")
	private ContenedorDao contenedorDao;

	@Resource(name="userN4Dao")
	private UserN4Dao userN4Dao;

	@Resource(name="recintoOrigenDao")
	private RecintoOrigenDao recintoOrigenDao;

	@Resource(name="specialDao")
	private SpecialDao specialDao;

	@Resource(name="bookingDao")
	private BookingDao bookingDao;

	@Resource(name="bookingItemDao")
	private BookingItemDao bookingItemDao;

	@Resource(name="tipoRecepcionVacioDao")
	private TipoRecepcionVacioDao tipoRecepcionVacioDao;

	@Resource(name="n4_webserviceTemplate")
	private N4WebServiceTemplate webServiceTemplate;

	@Resource(name="paqueteComercialDao")
	private PaqueteComercialDao paqueteComercialDao;
	
	@Resource(name="storedProceduresDao")
	private StoredProceduresDao storedProceduresDao;
	
	@Resource(name=DomainManager.BEAN_NAME)
	private DomainManager domainManager;
	
	// AgenciaAduanal

	public AgenciaAduanal getAgenciaAduanalWithId(Integer aValue) throws Exception
	{
		return agenciaAduanalDao.getWithId(aValue);
	}

	public List<SimpleEntry> getAgenciaAduanalDistinct(Integer usern4Id, String match, Integer maxResults) throws Exception
	{
		return agenciaAduanalDao.getDistinct(usern4Id, match, maxResults);
	}

	// LineaNaviera

	public LineaNaviera getLineaNavieraWithId(Integer aValue) throws Exception
	{
		return lineaNavieraDao.getWithId(aValue);
	}

	public LineaNaviera getLineaNavieraWithNombre(String aValue) throws Exception
	{
		return lineaNavieraDao.getWithNombre(aValue);
	}

	public List<SimpleEntry> getLineaNavieraByPattern(String match, Integer maxResults) throws Exception
	{
		return lineaNavieraDao.getDistinct(match, maxResults);
	}

	// Cliente

	public Cliente getClienteWithId(Integer aValue) throws Exception
	{
		return clienteDao.getWithId(aValue);
	}

	public List<SimpleEntry> getClienteDistinct(Integer usern4Id, String match, Integer maxResults) throws Exception
	{
		return clienteDao.getClienteDistinct(usern4Id, match, maxResults);
	}

	// Transportista

	public Transportista getTransportistaWithId(Integer aValue) throws Exception
	{
		return transportistaDao.getWithId(aValue);
	}

	public Transportista getTransportistaWithNombre(String aValue) throws Exception
	{
		return transportistaDao.getWithNombre(aValue);
	}

	public List<SimpleEntry> getTransportistaImpo(String match, Integer maxResults) throws Exception
	{
		return transportistaDao.getTransportistaDistinct(match, maxResults);
	}

	// OperadorTransportista

	public OperadorTransportista getOperadorTransportistaWithGkey(Integer aValue) throws Exception
	{
		return operadorTransportistaDao.getWithGkey(aValue);
	}

	public OperadorTransportista getOperadorTransportistaWithCardId(String aValue) throws Exception
	{
		return operadorTransportistaDao.getWithCardId(aValue);
	}

	public DaoResult<OperadorTransportista> queryOperadorTransportista(DaoQuery query, DaoOrder order) throws Exception
	{
		return operadorTransportistaDao.query(query, order);
	}

	public List<SimpleEntry> getOperadorTransportistaImpo(String match, Integer maxResults) throws Exception
	{
		return operadorTransportistaDao.getOperadorTransportistaDistinct(match, maxResults);
	}

	// Contenedor

	public Contenedor getContenedorWithId(Integer aValue) throws Exception
	{
		return contenedorDao.getWithId(aValue);
	}
	
	public Integer getCountUnitNbr(String aValue) throws Exception
	{
		return contenedorDao.getCountUnitNbr(aValue);
	}

	public List<SimpleEntry> getClaveContenedorImpo(String match, String consigne, Integer maxResults) throws Exception
	{
		return contenedorDao.getImpoDistinct(match, consigne, maxResults);
	}

	public List<SimpleEntry> getClaveContenedorHistorial(String match, Integer maxResults) throws Exception
	{
		return contenedorDao.getHistorialDistinct(match, maxResults);
	}

	public List<SimpleEntry> getClaveContenedorDesistimiento(String match, String consigne, Integer maxResults) throws Exception
	{
		return contenedorDao.getDesistimientoDistinct(match, consigne, maxResults);
	}
	
	public Contenedor getContenedorRecentWithUnitNbr(String aValue) throws Exception
	{
		return contenedorDao.getRecentWithUnitNbr(aValue);
	}
	
	public DaoResult<ContenedorStatus> getContenedorStatus(String numContenedor) throws Exception{
		return contenedorDao.getContenedorStatus(numContenedor);
	}
	
	// UserN4
	
	public UserN4 getUserWithGkey(Integer aValue) throws Exception
	{
		return userN4Dao.getWithGkey(aValue);
	}

	public UserN4 getWithId(String aValue) throws Exception
	{
		return userN4Dao.getWithId(aValue);
	}

	public UserN4 getWithName(String aValue) throws Exception
	{
		return userN4Dao.getWithName(aValue);
	}
	
	public UserN4 getWithCustId(String aValue) throws Exception
	{
		return userN4Dao.getWithCustId(aValue);
	}
	
	public DaoResult<UserN4> queryUserN4(DaoQuery query, DaoOrder order) throws Exception
	{
		return userN4Dao.query(query, order);
	}

	public List<SimpleEntry> getUserN4(String match, Integer maxResults) throws Exception
	{
		return userN4Dao.getUserN4Distinct(match, maxResults);
	}

	/*
	 * Booking
	 */
	
	public Booking getBookingWithId(Integer id) throws Exception
	{
		return bookingDao.getWithId(id);
	}

	public List<SimpleEntry> getBookingByPattern(String match, Integer maxResults) throws Exception
	{
		return bookingDao.getDistinct(match, maxResults);
	}
	
	public List<Booking> getBookingByName(String name, Integer maxResults) throws Exception
	{
		return bookingDao.getEqualsByName(name, maxResults);
	}
	
	public DaoResult<Booking> getBookingItemWithId(DaoQuery query, DaoOrder order) throws Exception
	{
		return bookingDao.getWithIdForType(query, order);
	}

	/*
	 * BookingItem
	 */
	
	public BookingItem getBookingItemWithId(Integer id) throws Exception
	{
		return bookingItemDao.getWithId(id);
	}
	
	public BookingItem getBookingItemWithGkey(Integer id, String type) throws Exception
	{
		return bookingItemDao.getWithBookingGkey(id, type);
	}
	
	public DaoResult<BookingItem> queryBookingItem(DaoQuery query, DaoOrder order) throws Exception
	{
		return bookingItemDao.queryBookingItem(query, order);
	}
	
	public DaoResult<BookingItem> getBookingItemWithIdForType(DaoQuery query, DaoOrder order) throws Exception
	{
		return bookingItemDao.getWithIdForType(query, order);
	}
	
	
	/*
	 * Special
	 */
	
	public Special getSpecialWithId(String id) throws Exception
	{
		return specialDao.getWithId(id);
	}

	public List<Special> querySpecial(DaoQuery query, DaoOrder order) throws Exception
	{
		return specialDao.query(query, order);
	}

	public List<Special> getAllSpecial() throws Exception
	{
		return specialDao.getAll();
	}

	/*
	 * RecintoOrigen
	 */
	
	public RecintoOrigen getRecintoOrigenWithId(String id) throws Exception
	{
		return recintoOrigenDao.getWithId(id);
	}

	public List<RecintoOrigen> getAllRecintoOrigen() throws Exception
	{
		return recintoOrigenDao.getAll();
	}

	/*
	 * TipoRecepcionVacio
	 */
	
	public TipoRecepcionVacio getTipoRecepcionVacioWithId(Integer id) throws Exception
	{
		return tipoRecepcionVacioDao.getWithId(id);
	}

	public TipoRecepcionVacio getTipoRecepcionVacioWithNombre(String aValue) throws Exception
	{
		return tipoRecepcionVacioDao.getWithNombre(aValue);
	}

	public List<SimpleEntry> getTipoRecepcionVacioByPattern(String match, Integer maxResults) throws Exception
	{
		return tipoRecepcionVacioDao.getDistinct(match, maxResults);
	}

	/*
	 * Appointment
	 */
	String Soli;
	public SolicitudAppointment processSaveAppointment(JsonNode node, Solicitud solicitud) throws DomainModelException, Exception
	{
		
		
		// check if is a new appointment
		Integer appointmentId = 0;
		if (node.has("appointmentId"))
			appointmentId = node.get("appointmentId").asInt();
	
		Boolean newAppointment = appointmentId == 0;

		// prepare solicitud appointment object
		SolicitudAppointment appointment = null;

		// current time
		Date now = new Date();

		// if new appointment
		if (newAppointment)
		{
			
			//throw new Exception("nuevo");
			appointment = new SolicitudAppointment();
			appointment.setDateCreated(now);
		}
		else
		{
			//cuando lo quiero editar entra aqui
			// check for the existing appointment on the database
			appointment = domainManager.getSolicitudAppointmentWithId(appointmentId);

			// if not found, do not proceed with the web service part
			// raise an error
			if (appointment == null)
				throw new Exception("SolicitudAppointment with id not found " + appointmentId);
		}

		//TODO check validation
//		String contenedor_aux=node.get("contenedor").get("label").asText();
//		String container_prefix=contenedor_aux.substring(0, 4);
//		String container_sufix=contenedor_aux.substring(4, contenedor_aux.length());
//		
//		Pattern pattern_aux = Pattern.compile("[a-zA-Z]{4}");
//		boolean prefix=pattern_aux.matcher(container_prefix).matches();
//		
//		Pattern pattern = Pattern.compile("\\d{7}");
//		boolean sufix= pattern.matcher(container_sufix).matches();
//		
//		if(prefix&&sufix!=true)
//		{
//			throw new DomainModelException("Container_invalid");
//		}
		
		/*
		 * Web Service Part
		 */

		N4WebServiceTemplate ws = getWebServiceTemplate();

		GateWebserviceRequestType request = new GateWebserviceRequestType();
		GateAppointmentRequestType appointmentWS = null;
		
		if (newAppointment)
			appointmentWS = new GateWebserviceRequestType.CreateAppointment();
		else
			appointmentWS = new GateWebserviceRequestType.UpdateAppointment();

		String dateStr = node.get("dayScheduled").asText();
		String timeStr = node.get("timeScheduled").asText();
		DateFormat format = (DateFormat) getBean("dateFormatter_yyyy-MM-dd_hh_mm_ss");
		Date scheduleDate = format.parse(dateStr + "  " + timeStr);
		
		// separate them
		appointmentWS.setAppointmentDate(ws.getXMLCalendarDateForDate(scheduleDate));
		appointmentWS.setAppointmentTime(ws.getXMLCalendarDateForTime(scheduleDate));
		//TODO check
		// gate and tranType
		
		Solicitud auxSolicitud= domainManager.getSolicitudWithFolio(solicitud.getFolio());
		appointmentWS.setGateId(auxSolicitud.getRecinto());
		appointmentWS.setTranType(node.get("tranType").asText());
		
//		if (node.has("referencia"))
//		{
//			appointmentWS.setExternalRefNbr(node.get("referencia").asText());
//		}

		// if no new appointment include the appointmentNbr
		if (!newAppointment)
		{
			((GateWebserviceRequestType.UpdateAppointment)appointmentWS).setAppointmentNbr(Long.parseLong(appointment.getAppointmentNbr()));
		}

		// OperadorTransportista
		Integer operadorGKey = node.get("operadorId").asInt();
		OperadorTransportista operador = getOperadorTransportistaWithGkey(operadorGKey);
		if (operador == null)
			throw new Exception("operador transportista with id not found " + operadorGKey);

		// Driver
		DriverBaseRequestType driver = new DriverBaseRequestType();
		// uses the getCardId instead of the id
		driver.setCardId(operador.getCardId());
		appointmentWS.setDriver(driver);

		// Transportista
		Integer transportistaId = node.get("transportistaId").asInt();
		Transportista transportista = getTransportistaWithId(transportistaId);
		System.out.println("transpot--"+getTransportistaWithId(transportistaId));
		if (transportista == null)
			throw new Exception("transportista with id not found " + transportistaId);

		// Truck
		TruckBaseRequestType truck = new TruckBaseRequestType();
		truck.setLicenseNbr(node.get("placas").getTextValue());
		String Nombre=transportista.getNombre();
		
		truck.setTruckingCoId(Nombre);
		appointmentWS.setTruck(truck);

		// Booking
		if (node.has("booking"))
		{
	
			GateAppointmentRequestType.Booking booking = new GateAppointmentRequestType.Booking();
			booking.setBookingNbr(node.get("booking").get("label").asText());
			booking.setLine(node.get("linea").get("label").asText());
			String FORMATER = "yyyy-MM-dd'T'HH:mm:ss";
		    DateFormat format2 = new SimpleDateFormat(FORMATER);
		    Date date2 = new Date();
		    XMLGregorianCalendar gDateFormatted =
		    DatatypeFactory.newInstance().newXMLGregorianCalendar(format2.format(date2));
		   
		    ContainerType container = new ContainerType();
			container.setUfvFlexDate2(gDateFormatted);
			appointmentWS.setBooking(booking);
			appointmentWS.setContainer(container);
		}
		
		// Container
//		Integer countContainers=null;
		if (node.has("contenedor"))
		{
			ContainerType container = new ContainerType();
			TUpdateFlags.Flag hold = new TUpdateFlags.Flag();
			container.setEqid(node.get("contenedor").get("label").asText());
			container.setType(node.get("tipo").get("label").asText());
			
			
			String FORMATER = "yyyy-MM-dd'T'HH:mm:ss";
		    DateFormat format2 = new SimpleDateFormat(FORMATER);

		    Date date2 = new Date();
		    XMLGregorianCalendar gDateFormatted =
		        DatatypeFactory.newInstance().newXMLGregorianCalendar(format2.format(date2));
			container.setUfvFlexDate2(gDateFormatted);
			
			//if(solicitud.getOperationType().toString().equals("IMPO"))
			
			
			if (node.has("linea") && solicitud.getOperationType().toString().equals("RECEPCION_VACIO"))
			{
				if(!node.get("linea").asText().equals("null"))
				{
					container.setLineId(node.get("linea").get("label").asText());
				}
				else
				{
					container.setLineId(node.get("extra").get("lineFromContainer").asText());
					//container.setUnitFlexString9(value);
				}
			} 
			
			
			if (node.has("undg") && solicitud.getOperationType().toString().equals("EXPO"))
			{
				container.setUnitFlexString15(node.get("undg").asText());
			}
			
			
			//TODO check
			if (node.has("special") && solicitud.getOperationType().toString().equals("RECEPCION_VACIO"))
			{
				Iterator<String> auxForSpecial=node.get("special").getFieldNames();
				if(auxForSpecial.hasNext())
				{
					if(!node.get("special").get("id").asText().equals("null"))
					{
						container.setUnitFlexString4(node.get("special").get("id").asText());
					}
				}
				else
				if(!auxForSpecial.hasNext())
				{
					if(!node.get("special").asText().equals("null"))
					{
					container.setUnitFlexString4(node.get("special").asText());
					}
				}
				else
				if(!node.get("extra").get("specialFromContainer").asText().equals("null"))
				{
					container.setUnitFlexString4(node.get("extra").get("specialFromContainer").asText());
				}
			} 
			
			//TODO check
			if (node.has("temporal") && solicitud.getOperationType().toString().equals("RECEPCION_VACIO"))
			{	
				Iterator<String> auxForTemporal=node.get("temporal").getFieldNames();
				if(auxForTemporal.hasNext())
				{
					if(!node.get("temporal").get("id").asText().equals("null"))
					{
						container.setUnitFlexString3(node.get("temporal").get("id").asText());
					}
				}
				else
				if(!auxForTemporal.hasNext())
				{
					if(!node.get("temporal").asText().equals("null"))
					{
						container.setUnitFlexString3(node.get("temporal").asText());
					}
				}
				else
				if(!node.get("extra").get("temporalFromContainer").asText().equals("null"))
				{
					container.setUnitFlexString3(node.get("extra").get("temporalFromContainer").asText());
				}
				
			} 
			
			
			
//			if(solicitud.getOperationType().toString().equals("IMPO")||
//					solicitud.getOperationType().toString().equals("EXPO"))
//			{
//				if(countContainers!=0)
//				{
			
			
					if (node.has("peso"))
					{
						
						Double value = node.get("peso").asDouble();
		
						if (value == 0d)
							throw new Exception("peso must not be 0");
		
						container.setGrossWeight(value);
					}			
		
					if(node.has("booking") && solicitud.getOperationType().toString().equals("EXPO"))
					{
						
						if (node.has("recintoOrigen"))
						{
							Iterator<String> auxForRecinto=node.get("recintoOrigen").getFieldNames();
							
							if(auxForRecinto.hasNext())
							{
								container.setUnitFlexString5(node.get("recintoOrigen").get("id").asText());
							}
							else
							{
								container.setUnitFlexString5(node.get("recintoOrigen").asText());
							}

						}
						if (node.has("special"))
						{
							Iterator<String> auxForSpecial=node.get("special").getFieldNames();
							if(auxForSpecial.hasNext())
							{
								container.setUnitFlexString4(node.get("special").get("id").asText());
							}
							else
							{
								container.setUnitFlexString4(node.get("special").asText());
							}
						} 
						
						if (node.has("sello1"))
						{
							container.setSeal1(node.get("sello1").asText());
						}
						if (node.has("sello2"))
						{
							container.setSeal2(node.get("sello2").asText());
						}
						if (node.has("sello3"))
						{
							container.setSeal3(node.get("sello3").asText());
						}
						if (node.has("sello4"))
						{
							container.setSeal4(node.get("sello4").asText());
						}
					}
					
//				}
//				else
//				{
//					throw new DomainModelException("Container_invalid");
//				}
//			}

			 
			String auxUndg = null;
			int positionSymbol=0; 

			if (node.has("hasHazard"))
			{
				if (node.get("hasHazard").asText().equals("true"))
				{
					if((domainManager.getDocumentoSolicitudBySolicitudId(solicitud)!=null && solicitud.getStatus().toString().equals("VALIDADO")))
					{
						appointmentWS.setContainer(container);
					}
					else
					{
						if(node.has("imo"))
						{
							if(node.get("imo").has("label"))
							{
								if(!node.get("imo").get("label").asText().equals("null"))
								{
									positionSymbol = node.get("imo").get("label").asText().indexOf("-");
 									auxUndg = node.get("imo").get("label").asText().substring(positionSymbol+1,node.get("imo").get("label").asText().length());
									if(!auxUndg.equals(""))
									{
									appointmentWS.setContainer(container);
									}
									else
									{
										throw new DomainModelException("La solicitud requiere documentación");
									}
								}
								
							}
						} 
						else
						{
							throw new DomainModelException("La solicitud requiere documentación");
						}
					} 
				}
			}
	
			//TODO test
			if (node.has("requireConnection"))
			{
				if (node.get("requireConnection").asText().equals("true"))
				{
					if(domainManager.getDocumentoSolicitudBySolicitudId(solicitud)!=null)
					{
					appointmentWS.setContainer(container);
				
					}
					else
					{
						throw new DomainModelException("La solicitud requiere documentacion");
					}
				}
			}
			
			appointmentWS.setContainer(container);
		}
		
		if (newAppointment)
			request.setCreateAppointment((GateWebserviceRequestType.CreateAppointment) appointmentWS);
		    
		else
			request.setUpdateAppointment((GateWebserviceRequestType.UpdateAppointment) appointmentWS);
		
		
		// sent the request
		GenericInvokeResponse response = ws.execute(request);
		
		//GenericInvokeResponse response_ = ws.execute();
		
		// get the response
		ResponseType responseType = ws.getResponseTypeFrom(response);

		// check for any errors
		if (ws.isError(responseType))
		{
			MessageType messageType = ws.getFirstMessageFrom(response);
			throw new DomainModelException(messageType.getMessage());
		}

		// get the domain object
		GateWebserviceResponseType obj = ws.getGateWebServiceResposeTypeFrom(response);

		// get the appointment of the new created appointment
		String appointmentNbr = newAppointment ?
			obj.getCreateAppointmentResponse().getAppointmentNbr()
			:
			obj.getUpdateAppointmentResponse().getAppointmentNbr();

		/*
		 * Database Part
		 */

		// set the solicitud again, as it is a proxy when resolved from the database
		appointment.setSolicitud(solicitud);
		// update appointment
		appointment.setDateUpdated(now);
		// save definition
		appointment.setDefinition(node.toString());
		// set the new appointmentNbr
		appointment.setAppointmentNbr(appointmentNbr);

		// update definition with appointmentNbr and other data
		appointment.updateDefinition();
		
		// save appointment
		domainManager.saveSolicitudAppointment(appointment);
		
		// if new appointment
		if (newAppointment)
		{
			// update definition after save
			// so appointmentId is updated on the definition field
			appointment.updateDefinition();
			// save again
			domainManager.saveSolicitudAppointment(appointment);
		}
	
		domainManager.publishEvent(new ActivityEvent(
			ActivityLogEntry.createForActionAndAppender(ActivityLogEntryConstants.SOLICITUD_APPOINTMENT_CREATED, appointment)));
        
		//aqui va tal vez///
		if(solicitud.getOperationType().toString().equals("IMPO"))
		{
			Iterator<String> auxForSpecial=node.get("special").getFieldNames();
			if(auxForSpecial.hasNext())
			{
				N4WebServiceTemplate ws_ = getWebServiceTemplate();
				String Contenedor =node.get("contenedor").get("label").asText();
				String Special= node.get("special").get("id").asText();
		        ws_.execute_(Contenedor,Special, "Se agregó Special Desde el Portal Web ");
			}
			else
			{
				//container.setUnitFlexString4("");
			}
		}
		return appointment;
	}
	private String conte_;
	
	public String getconte_()
	{
		return conte_;
	}

	public void setconte_(String conte_)
	{
		this.conte_ = conte_;
	}
	
	public void cancelAppointment(SolicitudAppointment appointment) throws DomainModelException, Exception
	{
		cancelAppointmentWithNumber(appointment.getAppointmentNbr());
		String  cont;
		String special;
		if (appointment.getSolicitud().getOperationType().toString().equals("IMPO"))
		{	
			String [] Contenedor;
			System.out.println("definition----" + appointment.getDefinition());
			HashMap<String,String>datos=new HashMap<String,String>();
		    Contenedor =appointment.getDefinition().split(",");
		    if(Contenedor[2].contains("appointmentId"))
		    {
		    	System.out.print("entre");
		    	  cont = Contenedor[3];
		    	  special= Contenedor[14];
		    }
		    else
		    {
		    	  cont = Contenedor[2];
		    	  special= Contenedor[13];
		    }
			
			String [] special1 = special.split(":");
			String special2 = special1[1].trim();
			System.out.println("definition----" + appointment.getDefinition());
		    
			System.out.println("special----"+special+"-");
			System.out.println("special----"+special2+"-");
			if(special2.equals("\"\"") || special2.equals("") || special2.equals("null") || special2.equals(null) || special2=="" || special2.trim()=="null")
			{	
				System.out.println("entre");
				domainManager.updateSolicitudAppointmentStatus(appointment, SolicitudAppointmentStatusEnum.CANCELED);
			}
			else	
			{ 
				System.out.println("special: lleno ");
					
					 System.out.println("contenedor 1 "+cont);
				    String [] cont1 = cont.split("\"");
				    String cont2 = cont1[5];
				    System.out.println("contenedor "+cont2);
					N4WebServiceTemplate ws_ = getWebServiceTemplate();
					String Special= "";
			        ws_.execute_cancelar(cont2,"", "Se Retiro Special desde el portal Web ");
			}
		}
		domainManager.updateSolicitudAppointmentStatus(appointment, SolicitudAppointmentStatusEnum.CANCELED);
	}
	

	public void cancelAppointmentWithNumber(String numberStr) throws DomainModelException, Exception
	{
		System.out.println("CANCELAR---2-");
		cancelAppointmentWithNumber(Long.parseLong(numberStr));
	}

	private void cancelAppointmentWithNumber(Long number) throws DomainModelException, Exception
	{
		System.out.println("CANCELAR---3-");
		N4WebServiceTemplate ws = getWebServiceTemplate();
       
		GateWebserviceRequestType request = new GateWebserviceRequestType();

		GateWebserviceRequestType.CancelAppointment cancelAppointment = new GateWebserviceRequestType.CancelAppointment();

		AppointmentListRequestType appointments = new AppointmentListRequestType();

		AppointmentRequestBaseType entry = new AppointmentRequestBaseType();
		entry.setAppointmentNbr(number);
		appointments.getAppointment().add(entry);

		cancelAppointment.setAppointments(appointments);
		request.setCancelAppointment(cancelAppointment);
		GenericInvokeResponse response = ws.execute(request);
		ResponseType responseType = ws.getResponseTypeFrom(response);
		if (ws.isError(responseType))
		{
			MessageType messageType = ws.getFirstMessageFrom(response);
			throw new DomainModelException(messageType.getMessage());
		}
		
		
		
	}

	public void cancelSolicitud(Solicitud solicitud) throws DomainModelException, Exception
	{	
		System.out.println("CANCELAR----");
		//TODO check and test4
		List <SolicitudAppointment> appointments= domainManager.getSolicitudAppointmentWithSolicitud(solicitud);
	
		if(appointments!=null)
		{	 
				for(int i=0;i<appointments.size();i++)
				{
					if(appointments.get(i)!=null&& !appointments.get(i).getStatus().getName().equals("Cancelado"))
						cancelAppointmentWithNumber(appointments.get(i).getAppointmentNbr());
				}
		}
		else
			throw new DomainModelException("Appointment_already_canceled");
	}
	//TODO
	public List<Date> getTimeSlotsForDate(Date aDate, Solicitud solicitud, String tit) throws Exception
	{
		List <SolicitudAppointment> app=domainManager.getSolicitudAppointmentWithSolicitud(solicitud);
		User user=domainManager.getUserFromSolicitud(solicitud);
		N4WebServiceTemplate ws = getWebServiceTemplate();
		
		Solicitud auxForSolicitud= domainManager.getSolicitudWithFolio(solicitud.getFolio());
		GateWebserviceRequestType request = new GateWebserviceRequestType();

		GateWebserviceRequestType.CreateAppointment appointment = new GateWebserviceRequestType.CreateAppointment();
		appointment.setAppointmentDate(ws.getXMLCalendarDateForDate(aDate));
		appointment.setAppointmentTime(ws.getXMLCalendarDateForTime(0, 0, 0));
		
		user.getEmail();
		if (user.getEmpresa().getCodigo().equals("ATP"))
		{
			if (auxForSolicitud.getOperationType().toString().equals("RECEPCION_VACIO")
					|| auxForSolicitud.getOperationType().toString().equals("ENTREGA_VACIO"))
			{

				if (auxForSolicitud.getRecinto() != null && !"DEPOSITO".equals(auxForSolicitud.getRecinto()))
				{
					System.out.println(">>>>>>>>>>>>>>>>> Recinto: "+ auxForSolicitud.getRecinto());
					System.out.println("Entro a ATP - RV / EV");
					appointment.setGateId("ATP");
				}
				else
				{
					System.out.println("DEPOSITO");
					appointment.setGateId("DEPOSITO");
				}
			}
			else
			{
				if (auxForSolicitud.getRecinto() != null)
				{
					appointment.setGateId(auxForSolicitud.getRecinto());
				}
				else
				{
					System.out.println("Entro sIN RECINTO");
					if(tit != null && tit.equals("Yes"))
						appointment.setGateId("TIT");
					else
						appointment.setGateId("ATP");
				}
			}

		}
		else
			if (user.getEmpresa().getCodigo().equals("IST"))
			{
				appointment.setGateId("IST");
			}
			else
				if (user.getEmpresa().getCodigo().equals("RFI"))
				{
					appointment.setGateId("RFI");
				}
		
		
		
		appointment.setTranType(solicitud.getOperationType().getTranType());
		appointment.setExternalRefNbr("TEST");

		DriverBaseRequestType driver = new DriverBaseRequestType();
		driver.setCardId("123456");
		appointment.setDriver(driver);

		TruckBaseRequestType truck = new TruckBaseRequestType();
		truck.setLicenseNbr("123");
		truck.setTruckingCoId("3G");
		appointment.setTruck(truck);

		ContainerType container = new ContainerType();
		container.setEqid("TEST1234567");
		
		String FORMATER = "yyyy-MM-dd'T'HH:mm:ss";
	    DateFormat format2 = new SimpleDateFormat(FORMATER);

	    Date date2 = new Date();
	    XMLGregorianCalendar gDateFormatted =
	        DatatypeFactory.newInstance().newXMLGregorianCalendar(format2.format(date2));
		container.setUfvFlexDate2(gDateFormatted);
		/*GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(new Date(System.currentTimeMillis()));
		XMLGregorianCalendar xmlDateCreated = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		
		container.setUfvFlexDate2(xmlDateCreated);*/
		
		
		appointment.setContainer(container);
	
		request.setCreateAppointment(appointment);

		GenericInvokeResponse resp = ws.execute(request);

		GateWebserviceResponseType obj = ws.getGateWebServiceResposeTypeFrom(resp);
		
		List<SlotType> types = obj.getCreateAppointmentResponse().getAvailableSlots();

		List<Date> col = new ArrayList<Date>();
		
		if (types != null)
		{
			for (SlotType eachSlotType : types)
			{
				col.add(eachSlotType.getSlotStart().toGregorianCalendar().getTime());
			}
		}

		return col;
	}

	public List<Date> getTimeSlotsForDate(Date aDate) throws Exception
	{
		N4WebServiceTemplate ws = getWebServiceTemplate();

		GateWebserviceRequestType request = new GateWebserviceRequestType();

		GateWebserviceRequestType.CreateAppointment appointment = new GateWebserviceRequestType.CreateAppointment();
		appointment.setAppointmentDate(ws.getXMLCalendarDateForDate(aDate));
		appointment.setAppointmentTime(ws.getXMLCalendarDateForTime(0, 0, 0));
		appointment.setGateId("ATP");
		appointment.setTranType("PUM");
		appointment.setExternalRefNbr("TEST");

		DriverBaseRequestType driver = new DriverBaseRequestType();
		driver.setCardId("123456");
		appointment.setDriver(driver);

		TruckBaseRequestType truck = new TruckBaseRequestType();
		truck.setLicenseNbr("123");
		truck.setTruckingCoId("3G");
		appointment.setTruck(truck);

		ContainerType container = new ContainerType();
		container.setEqid("TEST1234567");
		/*GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(new Date(System.currentTimeMillis()));
		XMLGregorianCalendar xmlDateCreated = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		
		container.setUfvFlexDate2(xmlDateCreated);*/
		String FORMATER = "yyyy-MM-dd'T'HH:mm:ss";
	    DateFormat format2 = new SimpleDateFormat(FORMATER);

	    Date date2 = new Date();
	    XMLGregorianCalendar gDateFormatted =
	        DatatypeFactory.newInstance().newXMLGregorianCalendar(format2.format(date2));
		container.setUfvFlexDate2(gDateFormatted);
		appointment.setContainer(container);
	
		request.setCreateAppointment(appointment);

		GenericInvokeResponse resp = ws.execute(request);

		GateWebserviceResponseType obj = ws.getGateWebServiceResposeTypeFrom(resp);
		
		List<SlotType> types = obj.getCreateAppointmentResponse().getAvailableSlots();

		List<Date> col = new ArrayList<Date>();
		
		if (types != null)
		{
			for (SlotType eachSlotType : types)
			{
				col.add(eachSlotType.getSlotStart().toGregorianCalendar().getTime());
			}
		}

		return col;
	}

	
	public PaqueteComercial getPaqueteComercialWithId(Integer idPaquete) throws Exception
	{
		return paqueteComercialDao.getWithId(idPaquete);
	}

	public List<SimpleEntry> getPaqueteComercial(String custid, String match, Integer maxResults) throws Exception
	{
		return paqueteComercialDao.getPaqueteComercial(custid, match, maxResults);
	}
	
	// Store Procedures
	
	public void callStoredProcedureCancellationAppointments(String v_cita, String v_note) throws Exception
	{
         storedProceduresDao.getCancellation(v_cita, v_note);
	}
	
	public void callAsignAgentClientReferenceAppointment(String app_nbr, Integer agent, Integer client, String reference) throws Exception
	{
		 storedProceduresDao.setInfoAgentClientReference(app_nbr, agent, client, reference);
	}
	
	public void callAsignAgentClientRefAppointment(String app_nbr, Integer agent, Integer client) throws Exception
	{
		 storedProceduresDao.setInfoAgentClientRef(app_nbr, agent, client);
	}
	
	public void callAsignBookingItemGkey(Integer booking_item_gkey, String app_nbr) throws Exception
	{
		 storedProceduresDao.setBookingItemGkey(booking_item_gkey, app_nbr);
	}
	
	/*
	 * Getters and setters
	 */

	public UserN4Dao getUserN4Dao()
	{
		return userN4Dao;
	}

	public void setUserN4Dao(UserN4Dao userN4Dao)
	{
		this.userN4Dao = userN4Dao;
	}

	public N4WebServiceTemplate getWebServiceTemplate()
	{
		return webServiceTemplate;
	}

	public void setWebServiceTemplate(N4WebServiceTemplate webServiceTemplate)
	{
		this.webServiceTemplate = webServiceTemplate;
	}

	public LineaNavieraDao getLineaNavieraDao()
	{
		return lineaNavieraDao;
	}

	public void setLineaNavieraDao(LineaNavieraDao lineaNavieraDao)
	{
		this.lineaNavieraDao = lineaNavieraDao;
	}

	public AgenciaAduanalDao getAgenciaAduanalDao()
	{
		return agenciaAduanalDao;
	}

	public void setAgenciaAduanalDao(AgenciaAduanalDao agenciaAduanalDao)
	{
		this.agenciaAduanalDao = agenciaAduanalDao;
	}

	public ClienteDao getClienteDao()
	{
		return clienteDao;
	}

	public void setClienteDao(ClienteDao clienteDao)
	{
		this.clienteDao = clienteDao;
	}

	public ContenedorDao getContenedorDao()
	{
		return contenedorDao;
	}

	public void setContenedorDao(ContenedorDao contenedorDao)
	{
		this.contenedorDao = contenedorDao;
	}

	public TransportistaDao getTransportistaDao()
	{
		return transportistaDao;
	}

	public void setTransportistaDao(TransportistaDao transportistaDao)
	{
		this.transportistaDao = transportistaDao;
	}

	public OperadorTransportistaDao getOperadorTransportistaDao()
	{
		return operadorTransportistaDao;
	}

	public void setOperadorTransportistaDao(OperadorTransportistaDao operadorTransportistaDao)
	{
		this.operadorTransportistaDao = operadorTransportistaDao;
	}

	public DomainManager getDomainManager()
	{
		return domainManager;
	}

	public void setDomainManager(DomainManager domainManager)
	{
		this.domainManager = domainManager;
	}

	public RecintoOrigenDao getRecintoOrigenDao()
	{
		return recintoOrigenDao;
	}

	public void setRecintoOrigenDao(RecintoOrigenDao recintoOrigenDao)
	{
		this.recintoOrigenDao = recintoOrigenDao;
	}

	public SpecialDao getSpecialDao()
	{
		return specialDao;
	}

	public void setSpecialDao(SpecialDao specialDao)
	{
		this.specialDao = specialDao;
	}

	public BookingDao getBookingDao()
	{
		return bookingDao;
	}

	public void setBookingDao(BookingDao bookingDao)
	{
		this.bookingDao = bookingDao;
	}

	public BookingItemDao getBookingItemDao()
	{
		return bookingItemDao;
	}

	public void setBookingItemDao(BookingItemDao bookingItemDao)
	{
		this.bookingItemDao = bookingItemDao;
	}

	public TipoRecepcionVacioDao getTipoRecepcionVacioDao()
	{
		return tipoRecepcionVacioDao;
	}

	public void setTipoRecepcionVacioDao(TipoRecepcionVacioDao tipoRecepcionVacioDao)
	{
		this.tipoRecepcionVacioDao = tipoRecepcionVacioDao;
	}
	
	public PaqueteComercialDao getPaqueteComercialDao()
	{
		return paqueteComercialDao;
	}

	public void setPaqueteComercialDao(PaqueteComercialDao paqueteComercialDao)
	{
		this.paqueteComercialDao = paqueteComercialDao;
	}

	public StoredProceduresDao getStoredProceduresDao()
	{
		return storedProceduresDao;
	}

	public void setStoredProceduresDao(StoredProceduresDao storedProceduresDao)
	{
		this.storedProceduresDao = storedProceduresDao;
	}


	
}
