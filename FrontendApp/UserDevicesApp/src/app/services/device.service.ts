import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {TokenStorageService} from "./token-storage.service";
import {DeviceDTO} from "../models/deviceDTO.model";
import {async} from "rxjs";

@Injectable({
  providedIn: "root"
})
export class DeviceService{

  constructor(private http: HttpClient,
              private token: TokenStorageService
  ){
  }

  httpOptions = {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + this.token.getToken()
    }
  }
  getDevices(){
    const url = environment.apiUrlDevices + 'devices/all'
    return this.http.get<any>(url, this.httpOptions)
  }

  getDevicesForUser(id: string){
    const url = environment.apiUrlDevices + 'devices/allByUser/' + id;
    return this.http.get<any>(url, this.httpOptions)
  }

   deleteDevice(deviceId : string){
    const url = environment.apiUrlDevices + 'devices/deleteDevice/' + deviceId;
    return this.http.delete(url, this.httpOptions);
  }

  updateDevice(deviceDTO: DeviceDTO){
    const url = environment.apiUrlDevices + 'devices/updateDevice';
    return this.http.put<any>(url, deviceDTO, this.httpOptions);
  }


  addDevice(deviceDTO: DeviceDTO){
    const url = environment.apiUrlDevices + 'devices/addDevice';
    return this.http.post<any>(url, deviceDTO, this.httpOptions);
  }

  getChartDataForDeviceOnDate(device: DeviceDTO, date: Date){
    date.setHours(0,0,0,0)
    const url = environment.apiUrlSensors + 'data/' + device.id + '/' + date.getTime();
    return this.http.get<any>(url, this.httpOptions);
  }

}
