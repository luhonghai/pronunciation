package com.cmg.vrc.worker;

import com.cmg.vrc.worker.util.Hash;

import java.io.Serializable;

/**
 * Created by cmg on 16/09/15.
 */
public class AnalyzingRequest implements Serializable {

    private static final long serialVersionUID = -2132247273618504468L;

    private String id;

    private String regionName;

    private String bucketName;

    private String keyName;

    private String word;

    private String elasticCacheAddress;

    private S3File dictionary;

    private S3File phoneDictionary;

    private S3File acousticModel;

    private S3File neighbourPhones;

    public String getAnalyzingKey() {
        return Hash.md5(regionName + bucketName + keyName + id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getElasticCacheAddress() {
        return elasticCacheAddress;
    }

    public void setElasticCacheAddress(String elasticCacheAddress) {
        this.elasticCacheAddress = elasticCacheAddress;
    }

    public S3File getDictionary() {
        return dictionary;
    }

    public void setDictionary(S3File dictionary) {
        this.dictionary = dictionary;
    }

    public S3File getNeighbourPhones() {
        return neighbourPhones;
    }

    public void setNeighbourPhones(S3File neighbourPhones) {
        this.neighbourPhones = neighbourPhones;
    }

    public S3File getPhoneDictionary() {
        return phoneDictionary;
    }

    public void setPhoneDictionary(S3File phoneDictionary) {
        this.phoneDictionary = phoneDictionary;
    }

    public S3File getAcousticModel() {
        return acousticModel;
    }

    public void setAcousticModel(S3File acousticModel) {
        this.acousticModel = acousticModel;
    }

    public static class S3File implements Serializable {

        private static final long serialVersionUID = -292983796512727515L;

        public S3File(String local, String key) {
            this.local = local;
            this.key = key;
        }

        private String local;
        private String key;

        public String getLocal() {
            return local;
        }

        public void setLocal(String local) {
            this.local = local;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
