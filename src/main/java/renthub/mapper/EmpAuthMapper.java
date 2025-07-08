package renthub.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface EmpAuthMapper {
    List<String> findPermissionKeysByEmpId(@Param("empId") Integer empId);
    List<String> findRoleKeysByEmpId(@Param("empId") Integer empId);
}
