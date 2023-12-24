package org.ourutils.mybatisextends.converts;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.ibatis.type.JdbcType;
import org.ourutils.mybatisextends.bo.Field2ColumnBo;
import org.ourutils.mybatisextends.bo.UpdateField2ColumnBo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-24T14:07:00+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_202 (Oracle Corporation)"
)
public class Field2ColumnConvertsImpl implements Field2ColumnConverts {

    @Override
    public Map<Field, UpdateField2ColumnBo> cloneUpdateField2ColumnBo(Map<Field, Field2ColumnBo> fieldField2ColumnBoMap) {
        if ( fieldField2ColumnBoMap == null ) {
            return null;
        }

        Map<Field, UpdateField2ColumnBo> map = new HashMap<Field, UpdateField2ColumnBo>( Math.max( (int) ( fieldField2ColumnBoMap.size() / .75f ) + 1, 16 ) );

        for ( java.util.Map.Entry<Field, Field2ColumnBo> entry : fieldField2ColumnBoMap.entrySet() ) {
            Field key = entry.getKey();
            UpdateField2ColumnBo value = field2ColumnBoToUpdateField2ColumnBo( entry.getValue() );
            map.put( key, value );
        }

        return map;
    }

    protected UpdateField2ColumnBo field2ColumnBoToUpdateField2ColumnBo(Field2ColumnBo field2ColumnBo) {
        if ( field2ColumnBo == null ) {
            return null;
        }

        String column = null;
        JdbcType jdbcType = null;
        String fieldName = null;
        Field field = null;

        column = field2ColumnBo.getColumn();
        jdbcType = field2ColumnBo.getJdbcType();
        fieldName = field2ColumnBo.getFieldName();
        field = field2ColumnBo.getField();

        UpdateField2ColumnBo updateField2ColumnBo = new UpdateField2ColumnBo( column, jdbcType, fieldName, field );

        return updateField2ColumnBo;
    }
}
