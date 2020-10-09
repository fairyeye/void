package com.example.demo.DB.jdbcTemplete;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/10/9 15:39
 */
public class JdbcTempleteBatchUpdateDemo {
/*    public ReturnT execute(Map<String, String> map, SchedulerTool tool) {
        // 1.获取当前的时间 2.减去两小时 3.去掉毫秒 4.转换成字符串 5.替换掉T
        String dateTime = LocalDateTime.now().plusHours(-2L).truncatedTo(ChronoUnit.SECONDS).toString().replaceAll("T", " ");

        // 查询SAP回传数据中检验结果为合格的数量以及单据的数量
        List<WmsQualifyProductVO> voList = wmsIqcRecordMapper.queryQualifyProduct(dateTime);

        List<String> kids = new ArrayList<>();

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("MATERIAL_LOT_UPDATE");
        String eventId = mtEventRepository.eventCreate(4L, eventCreateVO);

        for (WmsQualifyProductVO vo : voList) {
            if (vo.getQty().compareTo(vo.getQuantity()) == 0) {
                kids.add(vo.getKid());
                continue;
            }
            if (vo.getQty().compareTo(vo.getQuantity()) < 0) {

                List<WmsInstructionMaterialLotRel> materialLotRels = wmsInstructionMaterialLotRelRepository.select(new WmsInstructionMaterialLotRel() {{
                    setInstructionId(vo.getInstructionId());
                }});
                for (WmsInstructionMaterialLotRel materialLotRel : materialLotRels) {

                    MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                    mtMaterialLotVO2.setMaterialLotId(materialLotRel.getMaterialLotId());
                    mtMaterialLotVO2.setQualityStatus("OK");
                    mtMaterialLotVO2.setEventId(eventId);
                    mtMaterialLotRepository.materialLotUpdate(4L, mtMaterialLotVO2, "N");

                    MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
                    mtExtendVO10.setKeyId(materialLotRel.getMaterialLotId());
                    List<MtExtendVO5> extendVO5s = new ArrayList<>();
                    MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                    mtExtendVO5.setAttrName("STATUS");
                    mtExtendVO5.setAttrValue("TO_ACCEPT");
                    extendVO5s.add(mtExtendVO5);
                    mtExtendVO10.setAttrs(extendVO5s);
                    mtMaterialLotRepository.materialLotAttrPropertyUpdate(4L, mtExtendVO10);
                }
                kids.add(vo.getKid());
            }
        }

        String batchInsertSql = "INSERT INTO ssme_iqc_change(TENANT_ID, KID) VALUE (?, ?);";

        jdbcTemplate.batchUpdate(batchInsertSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, 4L);
                ps.setString(2, kids.get(i));
            }

            @Override
            public int getBatchSize() {
                return kids.size();
            }
        });

        return ReturnT.SUCCESS;
    }*/
}
