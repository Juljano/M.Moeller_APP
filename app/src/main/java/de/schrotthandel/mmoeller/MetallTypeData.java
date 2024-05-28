package de.schrotthandel.mmoeller;

import java.util.ArrayList;
import java.util.List;

public class MetallTypeData {
    private List<String> metalTypeList = new ArrayList<>();
    private List<Double> weightList = new ArrayList<>();
    private List<Double> priceList = new ArrayList<>();
    private List<Double> sumList = new ArrayList<>();


    public static MetallTypeData getInstance() {
        return MetallTypeDataSingleton.instance;
    }


    public MetallTypeData() {
    }

    public List<String> getMetalTypeList() {
        return metalTypeList;
    }

    public void setMetalTypeList(List<String> metalTypeList) {
        this.metalTypeList = metalTypeList;
    }

    public List<Double> getWeightList() {
        return weightList;
    }

    public void setWeightList(List<Double> weightList) {
        this.weightList = weightList;
    }

    public List<Double> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<Double> priceList) {
        this.priceList = priceList;
    }

    public List<Double> getSumList() {
        return sumList;
    }

    public void setSumList(List<Double> sumList) {
        this.sumList = sumList;
    }

    public void clearData() {
        metalTypeList.clear();
        weightList.clear();
        priceList.clear();
        sumList.clear();
    }

}

    class MetallTypeDataSingleton {
        static MetallTypeData instance = new MetallTypeData();
    }