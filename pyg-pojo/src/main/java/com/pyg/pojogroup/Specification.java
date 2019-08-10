package com.pyg.pojogroup;

import com.pyg.pojo.TbSpecification;
import com.pyg.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

/**
 * @Title Specification
 * @ProjectName pyg
 * @Description pyg/pyg-manager-web/src/main/webapp/js/controller/specificationController.js的DTO
 * @Author Hello.Ju
 * @Date 2019-08-08 23:20
 */
public class Specification implements Serializable {
    private TbSpecification specification;
    private List<TbSpecificationOption> specificationOptionList;

    public TbSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(TbSpecification specification) {
        this.specification = specification;
    }

    public List<TbSpecificationOption> getSpecificationOptionList() {
        return specificationOptionList;
    }

    public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
        this.specificationOptionList = specificationOptionList;
    }

    @Override
    public String toString() {
        return "Specification{" +
                "specification=" + specification +
                ", specificationOptionList=" + specificationOptionList +
                '}';
    }
}
