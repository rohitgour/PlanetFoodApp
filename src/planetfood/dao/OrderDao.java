
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planetfood.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import planetfood.dbutil.DBConnection;
import planetfood.pojo.Categories;
import planetfood.pojo.Order;
import planetfood.pojo.OrderDetail;

/**
 *
 * @author HP
 */
public class OrderDao {
    public static ArrayList<Order>getOrderByDate(java.util.Date startDate,java.util.Date endDate)throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("Select * from Orders where ord_date between ? and ?");
        long ms1 = startDate.getTime();
        long ms2 = endDate.getTime();
        java.sql.Date sdate = new java.sql.Date(ms1);
        java.sql.Date edate = new java.sql.Date(ms2);
        ps.setDate(1,sdate);
        ps.setDate(2,edate);
        ResultSet rs= ps.executeQuery();
        ArrayList<Order> orderList = new ArrayList<Order>();
        while(rs.next())
        {
            Order obj = new Order();
            obj.setOrdId(rs.getString("ord_id"));
            java.sql.Date d= rs.getDate("ord_date");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            String dateStr=sdf.format(d);
            obj.setOrdDate(dateStr);
            obj.setOrdAmount(rs.getDouble("ord_amount"));
            obj.setGst(rs.getDouble("gst"));
            obj.setGstAmount(rs.getDouble("gst_amount"));
            obj.setGrandTotal(rs.getDouble("grand_total"));
            obj.setDiscountAmount(rs.getDouble("discount"));
            orderList.add(obj);
        }
        return orderList;
    }
    public static String getNewId() throws SQLException
    {
        Connection conn=DBConnection.getConnection();
        PreparedStatement ps=conn.prepareStatement("Select count(*) from orders");
        int id=101;
        ResultSet  rs=ps.executeQuery();
        if(rs.next())
        {
            id=id+rs.getInt(1);
        }
        return "OD"+id;
    }
    public static boolean addOrder(Order order,ArrayList<OrderDetail> orderList)throws Exception{
        Connection conn=DBConnection.getConnection();
        PreparedStatement ps=conn.prepareStatement("Insert into orders values(?,?,?,?,?,?,?,?)");
        ps.setString(1, order.getOrdId());
        String dateStr=order.getOrdDate();
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
        java.util.Date d1=sdf.parse(dateStr);
        java.sql.Date d2=new java.sql.Date(d1.getTime());
        ps.setDate(2, d2);
        ps.setDouble(3,order.getGst());
        ps.setDouble(4, order.getGstAmount());
        ps.setDouble(5, order.getDiscountAmount());
        ps.setDouble(6, order.getGrandTotal());
        ps.setString(7,order.getUserId());
        ps.setDouble(8, order.getOrdAmount());
        int x=ps.executeUpdate();
        PreparedStatement ps2=conn.prepareStatement("Insert into order_details values(?,?,?,?)");
        int count=0,y;
        for(OrderDetail detail:orderList)
        {
            ps2.setString(1, detail.getOrdId());  //ps2.setDouble(3, detail.getQuantity());
            ps2.setString(2, detail.getProdId());
            ps2.setInt(3, (Integer.parseInt(detail.getQuantity())));
            ps2.setDouble(4, detail.getCost());
            y=ps2.executeUpdate();
            count=count+y;
        }
if(x>0 && count==orderList.size())
            return true;
        else
            return false;

    }
    
    
      public static ArrayList<Order>getAllOrder()throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("Select * from Orders ");
       
        ResultSet rs= ps.executeQuery();
        ArrayList<Order> orderList = new ArrayList<Order>();
        while(rs.next())
        {
            Order obj = new Order();
            obj.setOrdId(rs.getString("ord_id"));
            java.sql.Date d= rs.getDate("ord_date");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            String dateStr=sdf.format(d);
            obj.setOrdDate(dateStr);
            obj.setOrdAmount(rs.getDouble("ord_amount"));
            obj.setGst(rs.getDouble("gst"));
            obj.setGstAmount(rs.getDouble("gst_amount"));
            obj.setGrandTotal(rs.getDouble("grand_total"));
            obj.setDiscountAmount(rs.getDouble("discount"));
            orderList.add(obj);
        }
        return orderList;
    }
    
    
       public static ArrayList<Order>getAll()throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("Select ord_id,grand_total from orders ");
       
        ResultSet rs= ps.executeQuery();
        ArrayList<Order> PaisaList = new ArrayList<Order>();
        while(rs.next())
        {
            Order obj = new Order();
            obj.setOrdId(rs.getString("ord_id"));
            obj.setGrandTotal(rs.getDouble("grand_total"));
            
            PaisaList.add(obj);
        }
        return PaisaList;
    }
    
    
    
    
    
    
    
    
    
    
    
    
}

