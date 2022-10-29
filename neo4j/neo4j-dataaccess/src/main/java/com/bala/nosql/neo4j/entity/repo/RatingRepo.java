/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bala.nosql.neo4j.entity.repo;

import com.bala.nosql.neo4j.entity.Rating;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author michael.enudi
 */
@Repository
public interface RatingRepo extends Neo4jRepository<Rating, Long> {

}
