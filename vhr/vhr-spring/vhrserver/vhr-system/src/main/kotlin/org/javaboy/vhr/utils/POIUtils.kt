package org.javaboy.vhr.utils

import org.apache.poi.hpsf.DocumentSummaryInformationimport

org.javaboy.vhr.model.Positionimport org.springframework.http.HttpHeadersimport org.springframework.http.HttpStatusimport org.springframework.http.MediaTypeimport java.io.ByteArrayOutputStreamimport java.io.IOException
/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-11-11 23:25
 */
object POIUtils {
    fun employee2Excel(list: List<Employee>): ResponseEntity<ByteArray> {
        //1. 创建一个 Excel 文档
        val workbook = HSSFWorkbook()
        //2. 创建文档摘要
        workbook.createInformationProperties()
        //3. 获取并配置文档信息
        val docInfo: DocumentSummaryInformation = workbook.getDocumentSummaryInformation()
        //文档类别
        docInfo.setCategory("员工信息")
        //文档管理员
        docInfo.setManager("javaboy")
        //设置公司信息
        docInfo.setCompany("www.javaboy.org")
        //4. 获取文档摘要信息
        val summInfo: SummaryInformation = workbook.getSummaryInformation()
        //文档标题
        summInfo.setTitle("员工信息表")
        //文档作者
        summInfo.setAuthor("javaboy")
        // 文档备注
        summInfo.setComments("本文档由 javaboy 提供")
        //5. 创建样式
        //创建标题行的样式
        val headerStyle: HSSFCellStyle = workbook.createCellStyle()
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index)
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)
        val dateCellStyle: HSSFCellStyle = workbook.createCellStyle()
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"))
        val sheet: HSSFSheet = workbook.createSheet("员工信息表")
        //设置列的宽度
        sheet.setColumnWidth(0, 5 * 256)
        sheet.setColumnWidth(1, 12 * 256)
        sheet.setColumnWidth(2, 10 * 256)
        sheet.setColumnWidth(3, 5 * 256)
        sheet.setColumnWidth(4, 12 * 256)
        sheet.setColumnWidth(5, 20 * 256)
        sheet.setColumnWidth(6, 10 * 256)
        sheet.setColumnWidth(7, 10 * 256)
        sheet.setColumnWidth(8, 16 * 256)
        sheet.setColumnWidth(9, 12 * 256)
        sheet.setColumnWidth(10, 15 * 256)
        sheet.setColumnWidth(11, 20 * 256)
        sheet.setColumnWidth(12, 16 * 256)
        sheet.setColumnWidth(13, 14 * 256)
        sheet.setColumnWidth(14, 14 * 256)
        sheet.setColumnWidth(15, 12 * 256)
        sheet.setColumnWidth(16, 8 * 256)
        sheet.setColumnWidth(17, 20 * 256)
        sheet.setColumnWidth(18, 20 * 256)
        sheet.setColumnWidth(19, 15 * 256)
        sheet.setColumnWidth(20, 8 * 256)
        sheet.setColumnWidth(21, 25 * 256)
        sheet.setColumnWidth(22, 14 * 256)
        sheet.setColumnWidth(23, 15 * 256)
        sheet.setColumnWidth(24, 15 * 256)
        //6. 创建标题行
        val r0: HSSFRow = sheet.createRow(0)
        val c0: HSSFCell = r0.createCell(0)
        c0.setCellValue("编号")
        c0.setCellStyle(headerStyle)
        val c1: HSSFCell = r0.createCell(1)
        c1.setCellStyle(headerStyle)
        c1.setCellValue("姓名")
        val c2: HSSFCell = r0.createCell(2)
        c2.setCellStyle(headerStyle)
        c2.setCellValue("工号")
        val c3: HSSFCell = r0.createCell(3)
        c3.setCellStyle(headerStyle)
        c3.setCellValue("性别")
        val c4: HSSFCell = r0.createCell(4)
        c4.setCellStyle(headerStyle)
        c4.setCellValue("出生日期")
        val c5: HSSFCell = r0.createCell(5)
        c5.setCellStyle(headerStyle)
        c5.setCellValue("身份证号码")
        val c6: HSSFCell = r0.createCell(6)
        c6.setCellStyle(headerStyle)
        c6.setCellValue("婚姻状况")
        val c7: HSSFCell = r0.createCell(7)
        c7.setCellStyle(headerStyle)
        c7.setCellValue("民族")
        val c8: HSSFCell = r0.createCell(8)
        c8.setCellStyle(headerStyle)
        c8.setCellValue("籍贯")
        val c9: HSSFCell = r0.createCell(9)
        c9.setCellStyle(headerStyle)
        c9.setCellValue("政治面貌")
        val c10: HSSFCell = r0.createCell(10)
        c10.setCellStyle(headerStyle)
        c10.setCellValue("电话号码")
        val c11: HSSFCell = r0.createCell(11)
        c11.setCellStyle(headerStyle)
        c11.setCellValue("联系地址")
        val c12: HSSFCell = r0.createCell(12)
        c12.setCellStyle(headerStyle)
        c12.setCellValue("所属部门")
        val c13: HSSFCell = r0.createCell(13)
        c13.setCellStyle(headerStyle)
        c13.setCellValue("职称")
        val c14: HSSFCell = r0.createCell(14)
        c14.setCellStyle(headerStyle)
        c14.setCellValue("职位")
        val c15: HSSFCell = r0.createCell(15)
        c15.setCellStyle(headerStyle)
        c15.setCellValue("聘用形式")
        val c16: HSSFCell = r0.createCell(16)
        c16.setCellStyle(headerStyle)
        c16.setCellValue("最高学历")
        val c17: HSSFCell = r0.createCell(17)
        c17.setCellStyle(headerStyle)
        c17.setCellValue("专业")
        val c18: HSSFCell = r0.createCell(18)
        c18.setCellStyle(headerStyle)
        c18.setCellValue("毕业院校")
        val c19: HSSFCell = r0.createCell(19)
        c19.setCellStyle(headerStyle)
        c19.setCellValue("入职日期")
        val c20: HSSFCell = r0.createCell(20)
        c20.setCellStyle(headerStyle)
        c20.setCellValue("在职状态")
        val c21: HSSFCell = r0.createCell(21)
        c21.setCellStyle(headerStyle)
        c21.setCellValue("邮箱")
        val c22: HSSFCell = r0.createCell(22)
        c22.setCellStyle(headerStyle)
        c22.setCellValue("合同期限(年)")
        val c23: HSSFCell = r0.createCell(23)
        c23.setCellStyle(headerStyle)
        c23.setCellValue("合同起始日期")
        val c24: HSSFCell = r0.createCell(24)
        c24.setCellStyle(headerStyle)
        c24.setCellValue("合同终止日期")
        for (i in list.indices) {
            val emp: Employee = list[i]
            val row: HSSFRow = sheet.createRow(i + 1)
            row.createCell(0).setCellValue(emp.id)
            row.createCell(1).setCellValue(emp.name)
            row.createCell(2).setCellValue(emp.workID)
            row.createCell(3).setCellValue(emp.gender)
            val cell4: HSSFCell = row.createCell(4)
            cell4.setCellStyle(dateCellStyle)
            cell4.setCellValue(emp.birthday)
            row.createCell(5).setCellValue(emp.idCard)
            row.createCell(6).setCellValue(emp.wedlock)
            row.createCell(7).setCellValue(emp.nation.getName())
            row.createCell(8).setCellValue(emp.nativePlace)
            row.createCell(9).setCellValue(emp.politicsstatus.getName())
            row.createCell(10).setCellValue(emp.phone)
            row.createCell(11).setCellValue(emp.address)
            row.createCell(12).setCellValue(emp.department.getName())
            row.createCell(13).setCellValue(emp.jobLevel.name)
            row.createCell(14).setCellValue(emp.position.getName())
            row.createCell(15).setCellValue(emp.engageForm)
            row.createCell(16).setCellValue(emp.tiptopDegree)
            row.createCell(17).setCellValue(emp.specialty)
            row.createCell(18).setCellValue(emp.school)
            val cell19: HSSFCell = row.createCell(19)
            cell19.setCellStyle(dateCellStyle)
            cell19.setCellValue(emp.beginDate)
            row.createCell(20).setCellValue(emp.workState)
            row.createCell(21).setCellValue(emp.email)
            row.createCell(22).setCellValue(emp.contractTerm)
            val cell23: HSSFCell = row.createCell(23)
            cell23.setCellStyle(dateCellStyle)
            cell23.setCellValue(emp.beginContract)
            val cell24: HSSFCell = row.createCell(24)
            cell24.setCellStyle(dateCellStyle)
            cell24.setCellValue(emp.endContract)
            val cell25: HSSFCell = row.createCell(25)
            cell25.setCellStyle(dateCellStyle)
            cell25.setCellValue(emp.conversionTime)
        }
        val baos = ByteArrayOutputStream()
        val headers = HttpHeaders()
        try {
            headers.setContentDispositionFormData(
                "attachment",
                String("员工表.xls".toByteArray(charset("UTF-8")), "ISO-8859-1")
            )
            headers.contentType = MediaType.APPLICATION_OCTET_STREAM
            workbook.write(baos)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ResponseEntity<ByteArray>(baos.toByteArray(), headers, HttpStatus.CREATED)
    }

    /**
     * Excel 解析成 员工数据集合
     *
     * @param file
     * @param allNations
     * @param allPoliticsstatus
     * @param allDepartments
     * @param allPositions
     * @param allJobLevels
     * @return
     */
    fun excel2Employee(
        file: MultipartFile,
        allNations: List<Nation>,
        allPoliticsstatus: List<Politicsstatus>,
        allDepartments: List<Department>,
        allPositions: List<Position>,
        allJobLevels: List<JobLevel>
    ): List<Employee> {
        val list: MutableList<Employee> = ArrayList<Employee>()
        var employee: Employee? = null
        try {
            //1. 创建一个 workbook 对象
            val workbook = HSSFWorkbook(file.getInputStream())
            //2. 获取 workbook 中表单的数量
            val numberOfSheets: Int = workbook.getNumberOfSheets()
            for (i in 0 until numberOfSheets) {
                //3. 获取表单
                val sheet: HSSFSheet = workbook.getSheetAt(i)
                //4. 获取表单中的行数
                val physicalNumberOfRows: Int = sheet.getPhysicalNumberOfRows()
                for (j in 0 until physicalNumberOfRows) {
                    //5. 跳过标题行
                    if (j == 0) {
                        continue  //跳过标题行
                    }
                    //6. 获取行
                    val row: HSSFRow = sheet.getRow(j)
                        ?: continue  //防止数据中间有空行
                    //7. 获取列数
                    val physicalNumberOfCells: Int = row.getPhysicalNumberOfCells()
                    employee = Employee()
                    for (k in 0 until physicalNumberOfCells) {
                        val cell: HSSFCell = row.getCell(k)
                        when (cell.getCellType()) {
                            STRING -> {
                                val cellValue: String = cell.getStringCellValue()
                                when (k) {
                                    1 -> employee.name = cellValue
                                    2 -> employee.workID = cellValue
                                    3 -> employee.gender = cellValue
                                    5 -> employee.idCard = cellValue
                                    6 -> employee.wedlock = cellValue
                                    7 -> {
                                        val nationIndex = allNations.indexOf(Nation(cellValue))
                                        employee.nationId = allNations[nationIndex].id
                                    }

                                    8 -> employee.nativePlace = cellValue
                                    9 -> {
                                        val politicstatusIndex = allPoliticsstatus.indexOf(Politicsstatus(cellValue))
                                        employee.politicId = allPoliticsstatus[politicstatusIndex].id
                                    }

                                    10 -> employee.phone = cellValue
                                    11 -> employee.address = cellValue
                                    12 -> {
                                        val departmentIndex = allDepartments.indexOf(Department(cellValue))
                                        employee.departmentId = allDepartments[departmentIndex].id
                                    }

                                    13 -> {
                                        val jobLevelIndex = allJobLevels.indexOf(JobLevel(cellValue))
                                        employee.jobLevelId = allJobLevels[jobLevelIndex].id
                                    }

                                    14 -> {
                                        val positionIndex = allPositions.indexOf(Position(cellValue))
                                        employee.posId = allPositions[positionIndex].id
                                    }

                                    15 -> employee.engageForm = cellValue
                                    16 -> employee.tiptopDegree = cellValue
                                    17 -> employee.specialty = cellValue
                                    18 -> employee.school = cellValue
                                    20 -> employee.workState = cellValue
                                    21 -> employee.email = cellValue
                                }
                            }

                            else -> {
                                when (k) {
                                    4 -> employee.birthday = cell.getDateCellValue()
                                    19 -> employee.beginDate = cell.getDateCellValue()
                                    23 -> employee.beginContract = cell.getDateCellValue()
                                    24 -> employee.endContract = cell.getDateCellValue()
                                    22 -> employee.contractTerm = cell.getNumericCellValue()
                                    25 -> employee.conversionTime = cell.getDateCellValue()
                                }
                            }
                        }
                    }
                    list.add(employee)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return list
    }
}