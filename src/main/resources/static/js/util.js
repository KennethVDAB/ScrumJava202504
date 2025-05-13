"use strict";

export function byId(id) {
    return document.getElementById(id);
}

export function setText(id, text){
    byId(id).textContent = text;
}

export function showElementById(id){
    byId(id).hidden = false;
}

export function hideElementsById(...ids) {
    ids.forEach(id => byId(id).hidden = true);
}

export function isNullOrUndefined(value) {
    return value === null || value === undefined;
}