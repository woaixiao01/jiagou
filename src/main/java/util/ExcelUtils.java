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
 * ����Excel�Ĺ����࣬��excel������ʵ������Pojo������
 * @author �����
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
        	//1.�õ�����excel�ļ�
            Workbook workbook = WorkbookFactory.create(input);
            Sheet sheet = workbook.getSheet(sheetName);
            // ���ָ��sheet��,��ȡָ��sheet�е�����.
            if (!sheetName.trim().equals("")) {
                sheet = workbook.getSheet(sheetName);
            }
            // ��������sheet����������Ĭ��ָ���1��sheet.
            if (sheet == null) {
                sheet = workbook.getSheetAt(0);
            }
            //ͨ��Sheet�õ����е�����
            int rows = sheet.getPhysicalNumberOfRows();
            // ������ʱ�Ŵ���
            if (rows > 0) {
                List<Field> allFields = getMappedFiled(clazz, null);
                // ����һ��map���ڴ���е���ź�field.
                Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();
                // ��һ��Ϊ��ͷ
                Row rowHead = sheet.getRow(0);
                Map<String, Integer> cellMap = new HashMap<>();
                int cellNum = rowHead.getPhysicalNumberOfCells();
                for (int i = 0; i < cellNum; i++){
                    cellMap.put(rowHead.getCell(i).getStringCellValue().toLowerCase(), i);
                }
                for (Field field : allFields) {
                    // ����ע���field��ŵ�map��.
                    if (field.isAnnotationPresent(Excel.class)) {
                        Excel attr = field.getAnnotation(Excel.class);
                        // ����Name����ȡ��Ӧ��failed
                        int col = cellMap.get(attr.name().toLowerCase());
                        field.setAccessible(true);
                        fieldsMap.put(col, field);
                    }
                }
                // �ӵ�2�п�ʼȡ����
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
                        // ��map�еõ���Ӧ�е�field.
                        Field field = fieldsMap.get(j);
                        if (field == null) {
                            continue;
                        }
                        // ȡ������,�����ݶ�����������ֵ.
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
     * �õ�ʵ��������ͨ��ע��ӳ�������ݱ���ֶ�
     *
     * @param clazz
     * @param fields
     * @return
     */
    private List<Field> getMappedFiled(Class clazz, List<Field> fields) {
        if (fields == null) {
            fields = new ArrayList<Field>();
        }
        // �õ����ж����ֶ�
        Field[] allFields = clazz.getDeclaredFields();
        // �õ�����field����ŵ�һ��list��.
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
