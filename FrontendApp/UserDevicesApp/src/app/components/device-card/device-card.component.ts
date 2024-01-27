import {
  AfterContentInit,
  AfterViewInit,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output, SimpleChange,
  SimpleChanges,
  ViewChild
} from '@angular/core';
import {DeviceDTO} from "../../models/deviceDTO.model";
import {DeviceService} from "../../services/device.service";
import {SensorDataDTO} from "../../models/sensorDataDTO";
import {WebSocketDeviceService} from "../../services/websocket-device.service";
import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexChart,
  ApexXAxis,
  ApexDataLabels,
  ApexTitleSubtitle,
  ApexStroke,
  ApexGrid, ApexResponsive
} from "ng-apexcharts";
import {TokenService} from "../../services/token.service";
import {TokenStorageService} from "../../services/token-storage.service";

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  dataLabels: ApexDataLabels;
  grid: ApexGrid;
  stroke: ApexStroke;
  title: ApexTitleSubtitle;
};

@Component({
  selector: 'device-card',
  templateUrl: './device-card.component.html',
  styleUrls: ['./device-card.component.scss']
})
export class DeviceCardComponent implements AfterViewInit, OnInit {
  @Input() device!: DeviceDTO
  @Input() user!: string;
  @Input() isAdmin!: boolean;
  @Output() delete: EventEmitter<any> = new EventEmitter<any>();
  @Output() edit: EventEmitter<any> = new EventEmitter<any>();
  showChart: boolean = false;
  showChartInRealTime: boolean = false;
  index: number = 1;
  dateToShowPointFor = new Date();
  @ViewChild("chart") chart: ChartComponent | undefined;
  chartOptions: ChartOptions;
  @Output() warning= new EventEmitter<boolean>();
  warningLight = false

  httpOptions = {
    headers: {
      'Authorization': 'Bearer ' + this.token.getToken()
    }
  }
  editDevice() {
    this.edit.emit();
  }

  deleteDevice() {
    this.delete.emit();
  }

  dataPoints: any = [];

  constructor(private deviceService: DeviceService,
              private webSocketService: WebSocketDeviceService,
              private token: TokenStorageService
  ) {
    this.chartOptions = {
      series: [
        {
          name: "Energy",
          data: this.dataPoints
        }
      ],
      chart: {
        type: "line",
        zoom: {
          enabled: false
        },

      },
      dataLabels: {
        enabled: false
      },
      stroke: {
        show: true,
        curve: "smooth"
      },
      title: {
        text: "Energy Consumption/Hour",
        align: "center"
      },
      grid: {
        row: {
          colors: ["#f3f3f3", "transparent"], // takes an array which will be repeated on columns
          opacity: 0.5
        }
      },
      xaxis: {
        type: "datetime",
        tooltip: {
          enabled: false
        }
      }
    };
  }

  updateSeries() {
    this.chartOptions = {
      ...this.chartOptions, series: [{
        name: 'Energy',
        data: this.dataPoints
      }]
    }
    this.chart?.render();
  }


  getReadableDateFromTimestamp(timestamp: number) {
    return new Date(timestamp * 1000);
  }

  ngAfterViewInit() {
  }

  ngOnInit(): void {
    this.deviceService.getChartDataForDeviceOnDate(this.device, this.dateToShowPointFor).subscribe((response: any) => {
      let data: SensorDataDTO[] = response;
      for (let i = 0; i < data.length; i++) {
        this.dataPoints.push({
          x: this.getReadableDateFromTimestamp(data[i].timestamp),
          y: Number(data[i].measuredValue)
        });
      }
      this.updateSeries();
    });
    const stompClient = this.webSocketService.initializeWebSocketConnection();
    stompClient.connect(this.httpOptions.headers, (frame) => {
      stompClient.subscribe('/topic/ws/data/' + this.device.id, (response) => {
        let data: SensorDataDTO = JSON.parse(response.body);
        if (this.showChartInRealTime) {
          this.dataPoints.push({
            x: this.getReadableDateFromTimestamp(data.timestamp),
            y: Number(data.measuredValue)
          });
          if (this.showChart) {
            this.updateSeries();
          }
        }
      });
    })
    const stompClient2 = this.webSocketService.initializeWebSocketConnection();
    stompClient2.connect(this.httpOptions.headers, (frame) => {
      stompClient2.subscribe('/topic/ws/notification/' + this.device.id, (response) => {
        let data: number = JSON.parse(response.body);
        this.warning.emit(data > this.device.maxHourlyEnergyConsumption);
        this.warningLight = data > this.device.maxHourlyEnergyConsumption;
      });
    })
  }

  seeDataInRealTime() {
    this.dataPoints = [];
    this.showChartInRealTime = true;
    this.updateSeries();
  }

  retrieveDataBaseOnDate() {
    this.showChartInRealTime = false;
    this.deviceService.getChartDataForDeviceOnDate(this.device, this.dateToShowPointFor).subscribe((response: any) => {
      let data: SensorDataDTO[] = response;

      this.dataPoints = [];
      for (let i = 0; i < data.length; i++) {
        this.dataPoints.push({
          x: this.getReadableDateFromTimestamp(data[i].timestamp),
          y: Number(data[i].measuredValue)
        });
      }
      if (data.length == 0) {
        this.dataPoints = [];
      }
      this.updateSeries();
    });
  }
}
