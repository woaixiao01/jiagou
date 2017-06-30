package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;   

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import annotation.Excel;
import pojo.User;
/**
 * 处理Excel的工具类，将excel的数据实例化到Pojo对象中
 * @author 钟镇豪
 * @date 2017-06-30
 *
 * @param <T>
 */
public class ExcelUtils<T> {

    Class<T> clazz;

    public ExcelUtils(Class<T> clazz) {
        this.clazz = clazz;
    }

    public List<T> importExcel(String sheetName, InputStream input) {
        int maxCol = 0;
        List<T> list = new ArrayList<T>();
        try {
        	//1.拿到整个excel文件
            Workbook workbook = WorkbookFactory.create(input);
            Sheet sheet = workbook.getSheet(sheetName);
            // 如果指定sheet名,则取指定sheet中的内容.
            if (!sheetName.trim().equals("")) {
                sheet = workbook.getSheet(sheetName);
            }
            // 如果传入的sheet名不存在则默认指向第1个sheet.
            if (sheet == null) {
                sheet = workbook.getSheetAt(0);
            }
            //通过Sheet拿到所有的行数
            int rows = sheet.getPhysicalNumberOfRows();
            // 有数据时才处理
            if (rows > 0) {
                List<Field> allFields = getMappedFiled(clazz, null);
                // 定义一个map用于存放列的序号和field.
                Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();
                // 第一行为表头
                Row rowHead = sheet.getRow(0);
                Map<String, Integer> cellMap = new HashMap<>();
                int cellNum = rowHead.getPhysicalNumberOfCells();
                for (int i = 0; i < cellNum; i++){
                    cellMap.put(rowHead.getCell(i).getStringCellValue().toLowerCase(), i);
                }
                for (Field field : allFields) {
                    // 将有注解的field存放到map中.
                    if (field.isAnnotationPresent(Excel.class)) {
                        Excel attr = field.getAnnotation(Excel.class);
                        // 根据Name来获取相应的failed
                        int col = cellMap.get(attr.name().toLowerCase());
                        field.setAccessible(true);
                        fieldsMap.put(col, field);
                    }
                }
                // 从第2行开始取数据
                for (int i = 1; i < rows; i++) {
                    Row row = sheet.getRow(i);
                    T entity = null;
                    for (int j = 0; j < cellNum; j++) {
                        Cell cell = row.getCell(j);
                        if (cell == null) {
                            continue;
                        }
                        int cellType = cell.getCellType();
                        String c = "";
                        if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
                            DecimalFormat df = new DecimalFormat("0");
                            c = df.format(cell.getNumericCellValue());
                        } else if (cellType == HSSFCell.CELL_TYPE_BOOLEAN) {
                            c = String.valueOf(cell.getBooleanCellValue());
                        } else {
                            c = cell.getStringCellValue();
                        }
                        if (c == null || c.equals("")) {
                            continue;
                        }
                        entity = (entity == null ? clazz.newInstance() : entity);
                        // 从map中得到对应列的field.
                        Field field = fieldsMap.get(j);
                        if (field == null) {
                            continue;
                        }
                        // 取得类型,并根据对象类型设置值.
                        Class<?> fieldType = field.getType();
                        if (String.class == fieldType) {
                            field.set(entity, String.valueOf(c));
                        } else if ((Integer.TYPE == fieldType)
                                || (Integer.class == fieldType)) {
                            field.set(entity, Integer.valueOf(c));
                        } else if ((Long.TYPE == fieldType)
                                || (Long.class == fieldType)) {
                            field.set(entity, Long.valueOf(c));
                        } else if ((Float.TYPE == fieldType)
                                || (Float.class == fieldType)) {
                            field.set(entity, Float.valueOf(c));
                        } else if ((Short.TYPE == fieldType)
                                || (Short.class == fieldType)) {
                            field.set(entity, Short.valueOf(c));
                        } else if ((Double.TYPE == fieldType)
                                || (Double.class == fieldType)) {
                            field.set(entity, Double.valueOf(c));
                        } else if (Character.TYPE == fieldType) {
                            if (c.length() > 0) {
                                field.set(entity, c.charAt(0));
                            }
                        }
                    }
                    if (entity != null) {
                        list.add(entity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 得到实体类所有通过注解映射了数据表的字段
     *
     * @param clazz
     * @param fields
     * @return
     */
    private List<Field> getMappedFiled(Class clazz, List<Field> fields) {
        if (fields == null) {
            fields = new ArrayList<Field>();
        }
        // 得到所有定义字段
        Field[] allFields = clazz.getDeclaredFields();
        // 得到所有field并存放到一个list中.
        for (Field field : allFields) {
            if (field.isAnnotationPresent(Excel.class)) {
                fields.add(field);
            }
        }
        if (clazz.getSuperclass() != null
                && !clazz.getSuperclass().equals(Object.class)) {
            getMappedFiled(clazz.getSuperclass(), fields);
        }

        return fields;
    }
    
//    main
    public static void main(String[] args) {	
    	
    	
	    FileInputStream fileInputStream = null;
	    try {
	        fileInputStream = new FileInputStream("D://data.xlsx");
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    ExcelUtils<User> util = new ExcelUtils<>(User.class);
	    List<User> jalanHotelList = util.importExcel("user", fileInputStream);
	    // do something
    	for(User user:jalanHotelList){
    		System.out.println(user.toString());
    	}
    	
	}
    
    
    
    
    
    


}
