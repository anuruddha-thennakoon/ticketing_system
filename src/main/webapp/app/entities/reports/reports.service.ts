import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Reports } from './reports.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ReportsService {

    private resourceUrl = SERVER_API_URL + 'api/reports';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/reports';
    headers: any;

    constructor(private http: Http) {
        this.headers = new Headers();
        this.headers.append('Content-Type', 'application/json');
        this.headers.append('Access-Control-Allow-Origin', '*');
    }

    create(reports: Reports): Observable<Reports> {
        const copy = this.convert(reports);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(reports: Reports): Observable<Reports> {
        const copy = this.convert(reports);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Reports> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Reports.
     */
    private convertItemFromServer(json: any): Reports {
        const entity: Reports = Object.assign(new Reports(), json);
        return entity;
    }

    /**
     * Convert a Reports to a JSON which can be sent to the server.
     */
    private convert(reports: Reports): Reports {
        const copy: Reports = Object.assign({}, reports);
        return copy;
    }

    viewReport() {
        console.log('entered');
        this.http.get('http://localhost:8081/jasperserver/rest_v2/reports/reports/test_report2.pdf').subscribe(data => {
            console.log(data);
        });
    }
    
}
