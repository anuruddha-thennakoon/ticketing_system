import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Journey } from './journey.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class JourneyService {

    private resourceUrl = SERVER_API_URL + 'api/journeys';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/journeys';

    constructor(private http: Http) { }

    create(journey: Journey): Observable<Journey> {
        const copy = this.convert(journey);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(journey: Journey): Observable<Journey> {
        const copy = this.convert(journey);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Journey> {
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
     * Convert a returned JSON object to Journey.
     */
    private convertItemFromServer(json: any): Journey {
        const entity: Journey = Object.assign(new Journey(), json);
        return entity;
    }

    /**
     * Convert a Journey to a JSON which can be sent to the server.
     */
    private convert(journey: Journey): Journey {
        const copy: Journey = Object.assign({}, journey);
        return copy;
    }
}
