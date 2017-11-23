import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Passenger } from './passenger.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PassengerService {

    private resourceUrl = SERVER_API_URL + 'api/passengers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/passengers';

    constructor(private http: Http) { }

    create(passenger: Passenger): Observable<Passenger> {
        const copy = this.convert(passenger);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(passenger: Passenger): Observable<Passenger> {
        const copy = this.convert(passenger);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Passenger> {
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
     * Convert a returned JSON object to Passenger.
     */
    private convertItemFromServer(json: any): Passenger {
        const entity: Passenger = Object.assign(new Passenger(), json);
        return entity;
    }

    /**
     * Convert a Passenger to a JSON which can be sent to the server.
     */
    private convert(passenger: Passenger): Passenger {
        const copy: Passenger = Object.assign({}, passenger);
        return copy;
    }
}
