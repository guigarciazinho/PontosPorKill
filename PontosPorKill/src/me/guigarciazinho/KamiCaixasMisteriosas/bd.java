package me.guigarciazinho.KamiCaixasMisteriosas;

import java.sql.*;
import java.util.UUID;



public class bd{
	public Connection con = null;
	public Statement s;
	public PreparedStatement p;
	public String user = Main.getInstance().getConfig().getString("user");
	public String senha = Main.getInstance().getConfig().getString("senha");
	public String urlconf = Main.getInstance().getConfig().getString("url");
	public String dbname = Main.getInstance().getConfig().getString("dbname");

	

	public void conectar(){
		final String driver = "com.mysql.jdbc.Driver";
		final String url = "jdbc:mysql://"+urlconf +":3306/" + dbname;
		
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, senha);
		}catch(ClassNotFoundException erro){
			System.out.println("Driver não encontrado." + erro.toString());
		}catch(SQLException erro){
			System.out.println("Ocorreu um erro ao tentar se conectar com o banco de dados." + erro.toString());
		}
	}
	

	
	
	public void criar(){
		final String driver = "com.mysql.jdbc.Driver";
		final String url = "jdbc:mysql://"+urlconf +":3306/" + dbname;
		
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, senha);
			s = con.createStatement();
			s.executeUpdate("create table if not exists pontos("
                          +" id varchar(36) not null,"
                          +" nick varchar(16) not null,"
                          +" pontos int,"
                          +" caixas tinyint,"
                          +" primary key(id)"
						  +")default charset = utf8;");
			s.close();
			con.close();
		}catch(ClassNotFoundException erro){
			System.out.println("Driver não encontrado." + erro.toString());
		}catch(SQLException erro){
			System.out.println("Ocorreu um erro ao tentar se conectar com o banco de dados." + erro.toString());
		}
	}
	
	public boolean criarReg(UUID id, String nick){
		try{
		conectar();
	    s = con.createStatement();
	    ResultSet rs = s.executeQuery("SELECT * FROM pontos WHERE id = '"+id+"'");
	    if(rs.next()){
	        s.close();
	   	    rs.close();
	   	    con.close();
	    	return false;
	    }else{
	    String sql ="insert into pontos"
	    		       +" (id, nick, pontos, caixas)"
	    		       +" values"
	    		       +" ('"+id+"', '"+nick+"', '5', '3');";
	    s.executeUpdate(sql);
	    s.close();
	    rs.close();
	    con.close();
	    return true;
	    }
		}catch(SQLException e){
			System.out.println(e.toString());
			return false;
		}
	}
		
	
	public void win(UUID id){
		try{
		conectar();
	    s = con.createStatement();
	    String sql ="UPDATE pontos"
	    		       +" SET pontos = pontos + 1"
	    		       +" WHERE id = '"+ id+"';";
	    s.executeUpdate(sql);
	    s.close();
	    con.close();
		  
		}catch(SQLException e){
			System.out.println(e.toString());
			
		}
		
	}
	
	public void comprou(UUID id){
		try{
		conectar();
	    s = con.createStatement();
	    String sql ="UPDATE pontos"
	    		       +" SET pontos = pontos - 50, caixas = caixas + 1"
	    		       +" WHERE id = '"+ id+"';";
	    s.executeUpdate(sql);
	    s.close();
	    con.close();
		  
		}catch(SQLException e){
			System.out.println(e.toString());
			
		}
		
	}
	
	public boolean checar(UUID id){
		try{
			conectar();
		    String sql ="SELECT pontos FROM pontos"
		    		    +" WHERE id = '" +id +"';";
		    p = con.prepareStatement(sql);
		    ResultSet resultset = p.executeQuery();
		    if(resultset.next()){
		     int resultado = resultset.getInt("pontos");
		   	 resultset.close();
		   	 p.close();
		     if(resultado > 0){
		      String sql2 = "UPDATE pontos"
		      		     + " SET pontos = pontos-1"
		      		     + " WHERE id = '"+id+"';";
		      s = con.createStatement();
		      s.executeUpdate(sql2);
		      s.close();
		      con.close();
		      return true;
		    	 
		     }else{
		     con.close();
		   	 return false;
		     }
		    }else{
		    	p.close();
		   	    con.close();
		   	    resultset.close();
			    return false;
			    }
			}catch(SQLException e){
				System.out.println(e.toString());
				return false;
			}
	}
	public int checarPontos(UUID id){
		try{
			conectar();
		    String sql ="SELECT pontos FROM pontos"
		    		    +" WHERE id = '" +id +"';";
		    p = con.prepareStatement(sql);
		    ResultSet resultset = p.executeQuery();
		    if(resultset.next()){
		     int resultado = resultset.getInt("pontos");
		   	 resultset.close();
		   	 p.close();
		      con.close();
		      return resultado;
		    	 
		     }
		    return 0;
			}catch(SQLException e){
				System.out.println(e.toString());
				return 0;
			}
	}
	
	public int checarCaixas(UUID id){
		try{
			conectar();
		    String sql ="SELECT caixas FROM pontos"
		    		    +" WHERE id = '" +id +"';";
		    p = con.prepareStatement(sql);
		    ResultSet resultset = p.executeQuery();
		    if(resultset.next()){
		     int resultado = resultset.getInt("caixas");
		   	 resultset.close();
		   	 p.close();
		      con.close();
		      return resultado;
		    	 
		     }
		    return 0;
			}catch(SQLException e){
				System.out.println(e.toString());
				return 0;
			}
	}
	
	public void usarCaixas(UUID id){
		try{
			conectar();
			String sql = "UPDATE pontos"
		      		     + " SET caixas = caixas-1"
		      		     + " WHERE id = '"+id+"';";
		    s = con.createStatement();
		    s.executeUpdate(sql);
		    s.close();
		    con.close();
		    	 
		     
			}catch(SQLException e){
				System.out.println(e.toString());
			}
		
	}
	
	public String check(String value, UUID id){
		try{
		conectar();
	    String sql ="SELECT "+ value+" FROM perfil"
	    		    +" WHERE id = '" +id +"';";
	    p = con.prepareStatement(sql);
	    ResultSet resultset = p.executeQuery();
	    if(resultset.next()){
	    	String result = resultset.getString(value);
	        p.close();
	   	    con.close();
	   	    resultset.close();
	   	 return result;
	    	 
	    	 
	     
	    }else{
	    	   p.close();
	   	    con.close();
	   	    resultset.close();
		    return null;
		    }
		}catch(SQLException e){
			System.out.println(e.toString());
			return null;
		}
			
	}
		
	
		
		
	
	public void close(){
		try{
			con.close();
			System.out.println("Conexão com a base de dados encerrada.");
		}catch(SQLException e){
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
