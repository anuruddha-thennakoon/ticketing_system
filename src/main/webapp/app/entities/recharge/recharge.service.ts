import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Recharge } from './recharge.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class RechargeService {

    private resourceUrl = SERVER_API_URL + 'api/recharges';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/recharges';

    constructor(private http: Http) { }

    create(recharge: Recharge): Observable<Recharge> {
        const copy = this.convert(recharge);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(recharge: Recharge): Observable<Recharge> {
        const copy = this.convert(recharge);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Recharge> {
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
     * Convert a returned JSON object to Recharge.
     */
    private convertItemFromServer(json: any): Recharge {
        const entity: Recharge = Object.assign(new Recharge(), json);
        return entity;
    }

    /**
     * Convert a Recharge to a JSON which can be sent to the server.
     */
    private convert(recharge: Recharge): Recharge {
        const copy: Recharge = Object.assign({}, recharge);
        return copy;
    }
}
