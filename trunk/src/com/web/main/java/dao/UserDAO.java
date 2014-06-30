package dao;

import java.util.List;
import java.util.Map;

public interface UserDAO {

	public List<Map<String, Object>> getUsers() ;

	public Integer add(Map<String, Object> params) ;

	public Integer delete(int id) ;
}
