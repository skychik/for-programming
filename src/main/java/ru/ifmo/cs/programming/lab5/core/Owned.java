/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ifmo.cs.programming.lab5.core;

import ru.ifmo.cs.programming.lab5.domain.Employer;

/**
 *
 * @author admin
 */
public interface Owned {
    void setOwner(Employer owner);
}
