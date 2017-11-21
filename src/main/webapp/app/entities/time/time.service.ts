import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Time } from './time.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TimeService {

    private resourceUrl = SERVER_API_URL + 'api/times';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/times';

    constructor(private http: Http) { }

    create(time: Time): Observable<Time> {
        const copy = this.convert(time);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(time: Time): Observable<Time> {
        const copy = this.convert(time);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Time> {
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
     * Convert a returned JSON object to Time.
     */
    private convertItemFromServer(json: any): Time {
        const entity: Time = Object.assign(new Time(), json);
        return entity;
    }

    /**
     * Convert a Time to a JSON which can be sent to the server.
     */
    private convert(time: Time): Time {
        const copy: Time = Object.assign({}, time);
        return copy;
    }
}
