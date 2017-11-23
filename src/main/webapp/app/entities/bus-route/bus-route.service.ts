import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { BusRoute } from './bus-route.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class BusRouteService {

    private resourceUrl = SERVER_API_URL + 'api/bus-routes';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/bus-routes';

    constructor(private http: Http) { }

    create(busRoute: BusRoute): Observable<BusRoute> {
        const copy = this.convert(busRoute);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(busRoute: BusRoute): Observable<BusRoute> {
        const copy = this.convert(busRoute);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<BusRoute> {
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
     * Convert a returned JSON object to BusRoute.
     */
    private convertItemFromServer(json: any): BusRoute {
        const entity: BusRoute = Object.assign(new BusRoute(), json);
        return entity;
    }

    /**
     * Convert a BusRoute to a JSON which can be sent to the server.
     */
    private convert(busRoute: BusRoute): BusRoute {
        const copy: BusRoute = Object.assign({}, busRoute);
        return copy;
    }
}
