package net.huansi.equipment.equipmentapp.entity;

import java.util.List;

public class BomInfo {


    private List<ObjectsBean> objects;

    public List<ObjectsBean> getObjects() {
        return objects;
    }

    public void setObjects(List<ObjectsBean> objects) {
        this.objects = objects;
    }

    public static class ObjectsBean {
        /**
         * id : 27441b1e-257b-4912-a2f2-374d8de2fbd2
         * prmryAbrv : BLACK
         * prmryColorCd : 00A
         * plugColorwayCd : PR7
         * parentFcty : NKE
         * designRegCode : 01
         * mscCode : WGSG
         * seasonYr : 2018
         * objectId : 14924645924630
         * bomUpdateTimestamp : 8/1/18 1:35
         * developer : KRON, BRETT
         * bomUpdateUserid : BKRON
         * styleNbr : CI8611
         * bomId : 1492464
         * silhouetteCode : 80
         * prmryDesc : BLACK
         * prmryColorId : 1
         * productId : 3714838
         * cycleYear : FA2018
         * colorwayCd : 043
         * primDevRegAbrv : USA
         * colorwayId : 5924633
         * mscIdentifier : 5483
         * bomStatus : T1
         * seasonCd : FA
         * bomLineItems : [{"pcxSuppliedMatlId":0,"bomItmUpdateTimestamp":"8/1/18 1:27","itemType1":"STATEMENT","bomComponentId":44474125,"itemNbr":342169,"description":"PM#342158__ STATEMENT__ THIS IS A PROMO STYLE.__","is":"A","it":"I","vendLo":"XX","vendCd":"CONT","bomRowNbr":1,"bomItmSetupTimestamp":"8/1/18 1:27","vendNm":"CONTRACTOR","componentOrd":1,"vendId":0,"bomItmId":44910016},{"pcxSuppliedMatlId":0,"bomItmUpdateTimestamp":"8/5/18 6:47","itemType1":"STATEMENT","use":"THIS STYLE IS PROMO STYLE","bomComponentId":44474126,"itemNbr":69494,"description":"PM#069494__ STATEMENT__ W:0.0000 CENTIMETER__ THIS IS A SILHOUETTE RELATED STYLE.   FOR CROSS REFERENCE STYLE INFORMATION, PLEASE REFERENCE MEASUREMENTS AND CONSTRUCTION DETAILS OF BASE STYLE (SEASON & STYLE #) IN USE ","is":"A","it":"I","vendLo":"XX","vendCd":"CONT","bomRowNbr":2,"bomItmSetupTimestamp":"8/5/18 6:47","vendNm":"CONTRACTOR","componentOrd":1,"vendId":0,"bomItmId":44910017},{"pcxSuppliedMatlId":0,"bomItmUpdateTimestamp":"8/5/18 6:47","itemType1":"STATEMENT","use":"THIS STYLE IS PROMO STYLE","bomComponentId":44474126,"itemNbr":69494,"description":"PM#069494__ STATEMENT__ W:0.0000 CENTIMETER__ THIS IS A SILHOUETTE RELATED STYLE.   FOR CROSS REFERENCE STYLE INFORMATION, PLEASE REFERENCE MEASUREMENTS AND CONSTRUCTION DETAILS OF BASE STYLE (SEASON & STYLE #) IN USE ","is":"A","it":"I","vendLo":"XX","vendCd":"CONT","bomRowNbr":3,"bomItmSetupTimestamp":"8/5/18 6:47","vendNm":"CONTRACTOR","componentOrd":1,"vendId":0,"bomItmId":44910017},{"pcxSuppliedMatlId":0,"bomItmUpdateTimestamp":"8/5/18 6:47","itemType1":"STATEMENT","use":"THIS STYLE IS PROMO STYLE","bomComponentId":44474126,"itemNbr":69494,"description":"PM#069494__ STATEMENT__ W:0.0000 CENTIMETER__ THIS IS A SILHOUETTE RELATED STYLE.   FOR CROSS REFERENCE STYLE INFORMATION, PLEASE REFERENCE MEASUREMENTS AND CONSTRUCTION DETAILS OF BASE STYLE (SEASON & STYLE #) IN USE ","is":"A","it":"I","vendLo":"XX","vendCd":"CONT","bomRowNbr":4,"bomItmSetupTimestamp":"8/5/18 6:47","vendNm":"CONTRACTOR","componentOrd":1,"vendId":0,"bomItmId":44910017},{"pcxSuppliedMatlId":0,"bomItmUpdateTimestamp":"8/5/18 6:47","itemType1":"STATEMENT","use":"THIS STYLE IS PROMO STYLE","bomComponentId":44474126,"itemNbr":69494,"description":"PM#069494__ STATEMENT__ W:0.0000 CENTIMETER__ THIS IS A SILHOUETTE RELATED STYLE.   FOR CROSS REFERENCE STYLE INFORMATION, PLEASE REFERENCE MEASUREMENTS AND CONSTRUCTION DETAILS OF BASE STYLE (SEASON & STYLE #) IN USE ","is":"A","it":"I","vendLo":"XX","vendCd":"CONT","bomRowNbr":5,"bomItmSetupTimestamp":"8/5/18 6:47","vendNm":"CONTRACTOR","componentOrd":1,"vendId":0,"bomItmId":44910017}]
         * designRegAbrv : USA
         * styleNm : PCC_UAT_TEST_STYLE_1
         * silhouetteDesc : TEE
         * mscLevel3 : SPORT GOLF
         * mscLevel2 : GOLF
         * primDevRegCode : 01
         * mscLevel1 : WOMEN'S
         * developerUserId : BKRON
         * factoryCode : NKE
         * resourceType : /product/boms_edge
         * links : {"self":{"ref":"/dev/product/boms_edge/v1/27441b1e-257b-4912-a2f2-374d8de2fbd2"}}
         */

        private String id;
        private String prmryAbrv;
        private String prmryColorCd;
        private String plugColorwayCd;
        private String parentFcty;
        private String designRegCode;
        private String mscCode;
        private int seasonYr;
        private long objectId;
        private String bomUpdateTimestamp;
        private String developer;
        private String bomUpdateUserid;
        private String styleNbr;
        private int bomId;
        private int silhouetteCode;
        private String prmryDesc;
        private int prmryColorId;
        private int productId;
        private String cycleYear;
        private String colorwayCd;
        private String primDevRegAbrv;
        private int colorwayId;
        private int mscIdentifier;
        private String bomStatus;
        private String seasonCd;
        private String designRegAbrv;
        private String styleNm;
        private String silhouetteDesc;
        private String mscLevel3;
        private String mscLevel2;
        private String primDevRegCode;
        private String mscLevel1;
        private String developerUserId;
        private String factoryCode;
        private String resourceType;
        private LinksBean links;
        private List<BomLineItemsBean> bomLineItems;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPrmryAbrv() {
            return prmryAbrv;
        }

        public void setPrmryAbrv(String prmryAbrv) {
            this.prmryAbrv = prmryAbrv;
        }

        public String getPrmryColorCd() {
            return prmryColorCd;
        }

        public void setPrmryColorCd(String prmryColorCd) {
            this.prmryColorCd = prmryColorCd;
        }

        public String getPlugColorwayCd() {
            return plugColorwayCd;
        }

        public void setPlugColorwayCd(String plugColorwayCd) {
            this.plugColorwayCd = plugColorwayCd;
        }

        public String getParentFcty() {
            return parentFcty;
        }

        public void setParentFcty(String parentFcty) {
            this.parentFcty = parentFcty;
        }

        public String getDesignRegCode() {
            return designRegCode;
        }

        public void setDesignRegCode(String designRegCode) {
            this.designRegCode = designRegCode;
        }

        public String getMscCode() {
            return mscCode;
        }

        public void setMscCode(String mscCode) {
            this.mscCode = mscCode;
        }

        public int getSeasonYr() {
            return seasonYr;
        }

        public void setSeasonYr(int seasonYr) {
            this.seasonYr = seasonYr;
        }

        public long getObjectId() {
            return objectId;
        }

        public void setObjectId(long objectId) {
            this.objectId = objectId;
        }

        public String getBomUpdateTimestamp() {
            return bomUpdateTimestamp;
        }

        public void setBomUpdateTimestamp(String bomUpdateTimestamp) {
            this.bomUpdateTimestamp = bomUpdateTimestamp;
        }

        public String getDeveloper() {
            return developer;
        }

        public void setDeveloper(String developer) {
            this.developer = developer;
        }

        public String getBomUpdateUserid() {
            return bomUpdateUserid;
        }

        public void setBomUpdateUserid(String bomUpdateUserid) {
            this.bomUpdateUserid = bomUpdateUserid;
        }

        public String getStyleNbr() {
            return styleNbr;
        }

        public void setStyleNbr(String styleNbr) {
            this.styleNbr = styleNbr;
        }

        public int getBomId() {
            return bomId;
        }

        public void setBomId(int bomId) {
            this.bomId = bomId;
        }

        public int getSilhouetteCode() {
            return silhouetteCode;
        }

        public void setSilhouetteCode(int silhouetteCode) {
            this.silhouetteCode = silhouetteCode;
        }

        public String getPrmryDesc() {
            return prmryDesc;
        }

        public void setPrmryDesc(String prmryDesc) {
            this.prmryDesc = prmryDesc;
        }

        public int getPrmryColorId() {
            return prmryColorId;
        }

        public void setPrmryColorId(int prmryColorId) {
            this.prmryColorId = prmryColorId;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getCycleYear() {
            return cycleYear;
        }

        public void setCycleYear(String cycleYear) {
            this.cycleYear = cycleYear;
        }

        public String getColorwayCd() {
            return colorwayCd;
        }

        public void setColorwayCd(String colorwayCd) {
            this.colorwayCd = colorwayCd;
        }

        public String getPrimDevRegAbrv() {
            return primDevRegAbrv;
        }

        public void setPrimDevRegAbrv(String primDevRegAbrv) {
            this.primDevRegAbrv = primDevRegAbrv;
        }

        public int getColorwayId() {
            return colorwayId;
        }

        public void setColorwayId(int colorwayId) {
            this.colorwayId = colorwayId;
        }

        public int getMscIdentifier() {
            return mscIdentifier;
        }

        public void setMscIdentifier(int mscIdentifier) {
            this.mscIdentifier = mscIdentifier;
        }

        public String getBomStatus() {
            return bomStatus;
        }

        public void setBomStatus(String bomStatus) {
            this.bomStatus = bomStatus;
        }

        public String getSeasonCd() {
            return seasonCd;
        }

        public void setSeasonCd(String seasonCd) {
            this.seasonCd = seasonCd;
        }

        public String getDesignRegAbrv() {
            return designRegAbrv;
        }

        public void setDesignRegAbrv(String designRegAbrv) {
            this.designRegAbrv = designRegAbrv;
        }

        public String getStyleNm() {
            return styleNm;
        }

        public void setStyleNm(String styleNm) {
            this.styleNm = styleNm;
        }

        public String getSilhouetteDesc() {
            return silhouetteDesc;
        }

        public void setSilhouetteDesc(String silhouetteDesc) {
            this.silhouetteDesc = silhouetteDesc;
        }

        public String getMscLevel3() {
            return mscLevel3;
        }

        public void setMscLevel3(String mscLevel3) {
            this.mscLevel3 = mscLevel3;
        }

        public String getMscLevel2() {
            return mscLevel2;
        }

        public void setMscLevel2(String mscLevel2) {
            this.mscLevel2 = mscLevel2;
        }

        public String getPrimDevRegCode() {
            return primDevRegCode;
        }

        public void setPrimDevRegCode(String primDevRegCode) {
            this.primDevRegCode = primDevRegCode;
        }

        public String getMscLevel1() {
            return mscLevel1;
        }

        public void setMscLevel1(String mscLevel1) {
            this.mscLevel1 = mscLevel1;
        }

        public String getDeveloperUserId() {
            return developerUserId;
        }

        public void setDeveloperUserId(String developerUserId) {
            this.developerUserId = developerUserId;
        }

        public String getFactoryCode() {
            return factoryCode;
        }

        public void setFactoryCode(String factoryCode) {
            this.factoryCode = factoryCode;
        }

        public String getResourceType() {
            return resourceType;
        }

        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }

        public LinksBean getLinks() {
            return links;
        }

        public void setLinks(LinksBean links) {
            this.links = links;
        }

        public List<BomLineItemsBean> getBomLineItems() {
            return bomLineItems;
        }

        public void setBomLineItems(List<BomLineItemsBean> bomLineItems) {
            this.bomLineItems = bomLineItems;
        }

        public static class LinksBean {
            /**
             * self : {"ref":"/dev/product/boms_edge/v1/27441b1e-257b-4912-a2f2-374d8de2fbd2"}
             */

            private SelfBean self;

            public SelfBean getSelf() {
                return self;
            }

            public void setSelf(SelfBean self) {
                this.self = self;
            }

            public static class SelfBean {
                /**
                 * ref : /dev/product/boms_edge/v1/27441b1e-257b-4912-a2f2-374d8de2fbd2
                 */

                private String ref;

                public String getRef() {
                    return ref;
                }

                public void setRef(String ref) {
                    this.ref = ref;
                }
            }
        }

        public static class BomLineItemsBean {
            /**
             * pcxSuppliedMatlId : 0
             * bomItmUpdateTimestamp : 8/1/18 1:27
             * itemType1 : STATEMENT
             * bomComponentId : 44474125
             * itemNbr : 342169
             * description : PM#342158__ STATEMENT__ THIS IS A PROMO STYLE.__
             * is : A
             * it : I
             * vendLo : XX
             * vendCd : CONT
             * bomRowNbr : 1
             * bomItmSetupTimestamp : 8/1/18 1:27
             * vendNm : CONTRACTOR
             * componentOrd : 1
             * vendId : 0
             * bomItmId : 44910016
             * use : THIS STYLE IS PROMO STYLE
             */

            private int pcxSuppliedMatlId;
            private String bomItmUpdateTimestamp;
            private String itemType1;
            private int bomComponentId;
            private int itemNbr;
            private String description;
            private String is;
            private String it;
            private String vendLo;
            private String vendCd;
            private int bomRowNbr;
            private String bomItmSetupTimestamp;
            private String vendNm;
            private int componentOrd;
            private int vendId;
            private int bomItmId;
            private String use;

            public int getPcxSuppliedMatlId() {
                return pcxSuppliedMatlId;
            }

            public void setPcxSuppliedMatlId(int pcxSuppliedMatlId) {
                this.pcxSuppliedMatlId = pcxSuppliedMatlId;
            }

            public String getBomItmUpdateTimestamp() {
                return bomItmUpdateTimestamp;
            }

            public void setBomItmUpdateTimestamp(String bomItmUpdateTimestamp) {
                this.bomItmUpdateTimestamp = bomItmUpdateTimestamp;
            }

            public String getItemType1() {
                return itemType1;
            }

            public void setItemType1(String itemType1) {
                this.itemType1 = itemType1;
            }

            public int getBomComponentId() {
                return bomComponentId;
            }

            public void setBomComponentId(int bomComponentId) {
                this.bomComponentId = bomComponentId;
            }

            public int getItemNbr() {
                return itemNbr;
            }

            public void setItemNbr(int itemNbr) {
                this.itemNbr = itemNbr;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getIs() {
                return is;
            }

            public void setIs(String is) {
                this.is = is;
            }

            public String getIt() {
                return it;
            }

            public void setIt(String it) {
                this.it = it;
            }

            public String getVendLo() {
                return vendLo;
            }

            public void setVendLo(String vendLo) {
                this.vendLo = vendLo;
            }

            public String getVendCd() {
                return vendCd;
            }

            public void setVendCd(String vendCd) {
                this.vendCd = vendCd;
            }

            public int getBomRowNbr() {
                return bomRowNbr;
            }

            public void setBomRowNbr(int bomRowNbr) {
                this.bomRowNbr = bomRowNbr;
            }

            public String getBomItmSetupTimestamp() {
                return bomItmSetupTimestamp;
            }

            public void setBomItmSetupTimestamp(String bomItmSetupTimestamp) {
                this.bomItmSetupTimestamp = bomItmSetupTimestamp;
            }

            public String getVendNm() {
                return vendNm;
            }

            public void setVendNm(String vendNm) {
                this.vendNm = vendNm;
            }

            public int getComponentOrd() {
                return componentOrd;
            }

            public void setComponentOrd(int componentOrd) {
                this.componentOrd = componentOrd;
            }

            public int getVendId() {
                return vendId;
            }

            public void setVendId(int vendId) {
                this.vendId = vendId;
            }

            public int getBomItmId() {
                return bomItmId;
            }

            public void setBomItmId(int bomItmId) {
                this.bomItmId = bomItmId;
            }

            public String getUse() {
                return use;
            }

            public void setUse(String use) {
                this.use = use;
            }
        }
    }
}
