package com.texaconnect.texa.ui.devices;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.texaconnect.texa.event.MQTTSubscribeEvent;
import com.texaconnect.texa.model.DeviceItem;
import com.texaconnect.texa.model.Element;
import com.texaconnect.texa.model.GroupItem;
import com.texaconnect.texa.model.Node;
import com.texaconnect.texa.model.Resource;
import com.texaconnect.texa.repository.DeviceRepository;
import com.texaconnect.texa.util.AbsentLiveData;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class MqttViewModel extends AndroidViewModel {

    //    private final LiveData<HashMap<String, Connection>> connectionData;
    private final LiveData<Resource<List<DeviceItem>>> deviceData;
    private final LiveData<Resource<List<GroupItem>>> groupData;
    private final LiveData<Resource<String>> addGroupData;
    private MutableLiveData<List<Object>> deviceDataAll = new MutableLiveData<>();
    private MutableLiveData<List<Object>> groupDataAll = new MutableLiveData<>();
    private final MutableLiveData<String> device = new MutableLiveData<>();
    private final MutableLiveData<String> group = new MutableLiveData<>();
    private final MutableLiveData<String> addGroup = new MutableLiveData<>();
    Map<String, Object> nodeMap;
    @Inject
    public MqttViewModel(@NonNull Application application, DeviceRepository deviceRepository) {
        super(application);

        deviceData = Transformations.switchMap(device, data -> {
            if (data == null) {
                return AbsentLiveData.create();
            } else {
                return deviceRepository.getDevices();
            }
        });

        groupData = Transformations.switchMap(group, data -> {
            if (data == null) {
                return AbsentLiveData.create();
            } else {
                return deviceRepository.getGroups();
            }
        });

        addGroupData = Transformations.switchMap(addGroup, data -> {
            if (data == null) {
                return AbsentLiveData.create();
            } else {
                return deviceRepository.addGroup(data);
            }
        });
    }

    public LiveData<Resource<List<DeviceItem>>> observeDeviceData() {

        return deviceData;
    }

    public LiveData<Resource<List<GroupItem>>> observeGroupData() {

        return groupData;
    }

    public LiveData<List<Object>> observeDeviceDataAll() {

        return deviceDataAll;
    }

    public LiveData<List<Object>> observeGroupDataAll() {

        return groupDataAll;
    }

    public LiveData<Resource<String>> observeAddGroupData() {

        return addGroupData;
    }

    public Node getNodeByTopic(String topic){
        return (Node) nodeMap.get(topic);
    }

    public void getDeviceData() {
        device.setValue("1sdfgfgfg");
    }

    public void getGroupData() {
        group.setValue("1sdfgfgfg");
    }

    public void addGroup(String name) {
        addGroup.setValue(name);
    }

    public void setSubscription() {

        nodeMap = new HashMap<>();
        List<Object> items = new ArrayList<>();
        for (DeviceItem deviceItem : deviceData.getValue().data) {

            deviceItem.device.subscriptionTopic = "in/d1/"+deviceItem.device.serial+"/status";
            nodeMap.put(deviceItem.device.subscriptionTopic, deviceItem);
            items.add(deviceItem);
            for (Node node : deviceItem.device.nodes) {
                node.subscriptionTopic = "in/d1/"+deviceItem.device.serial+"/tx/"+node.nodeId;
                node.sendMessageTopic = "in/d1/"+deviceItem.device.serial+"/rx/"+node.nodeId;
                nodeMap.put(node.subscriptionTopic, node);
                items.add(node);
            }
        }

        deviceDataAll.setValue(items);
        EventBus.getDefault().post(new MQTTSubscribeEvent(nodeMap));

    }

    public void addItems(List<GroupItem> groupItems) {
        if (groupItems != null) {
            List<Object> items = new ArrayList<>();
            List<DeviceItem> deviceItems = deviceData.getValue().data;

            for (GroupItem groupItem : groupItems) {
                items.add(groupItem);
                for (Element element : groupItem.elements) {

                    for (Map.Entry<String, Object> e : nodeMap.entrySet()) {
                        if (e.getValue() instanceof Node) {
                            Node node = (Node) e.getValue();
                            if (node.id.equals(element.node.id)) {
                                element.node = node;
                            }
                        }else if (e.getValue() instanceof DeviceItem) {
                            DeviceItem deviceItem = (DeviceItem) e.getValue();
                            if (deviceItem.device.id.equals(element.device.id)) {
                                element.device = deviceItem.device;
                            }
                        }

                    }
                    items.add(element);
                }
            }
            groupDataAll.setValue(items);
        }else {
            groupDataAll.setValue(null);

        }
    }
}