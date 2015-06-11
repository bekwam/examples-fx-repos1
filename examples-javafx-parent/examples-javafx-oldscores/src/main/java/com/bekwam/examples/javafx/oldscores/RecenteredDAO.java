/*
 * Copyright 2015 Bekwam, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bekwam.examples.javafx.oldscores;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * DAO for JSON scores files
 *
 * @author carl_000
 */
public class RecenteredDAO {

    private Logger logger = LoggerFactory.getLogger(RecenteredDAO.class);

    private boolean initialized = false;

    private Scores verbalScores;
    private Scores mathScores;

    public void init() throws IOException {

        BufferedReader br = null;
        Gson gson = new Gson();

        try {
            InputStream is = this.getClass().getResourceAsStream(Constants.MATH_RECENTERED_JSON_FILE);
            br = new BufferedReader(new InputStreamReader(is));
            String json = "";
            String line;
            while( (line=br.readLine()) != null ) {
                json += line;
            }
            mathScores = gson.fromJson(json, Scores.class);
        } finally {
            if( br != null ) {
                try {
                    br.close();
                } catch(IOException exc) {
                    if( logger.isWarnEnabled() ) {
                        logger.warn("[INIT] cannot close buffered reader for math json file", exc);
                    }
                }
            }
        }

        try {
            InputStream is = this.getClass().getResourceAsStream(Constants.VERBAL_RECENTERED_JSON_FILE);
            br = new BufferedReader(new InputStreamReader(is));
            String json = "";
            String line;
            while( (line=br.readLine()) != null ) {
                json += line;
            }
            verbalScores = gson.fromJson( json, Scores.class );
        } finally {
            try {
                br.close();
            } catch(IOException exc) {
                if( logger.isWarnEnabled() ) {
                    logger.warn("[INIT] cannot close buffered reader for verbal json file", exc);
                }
            }
        }

        initialized = true;
    }

    public Integer lookup1995MathScore(Integer recenteredScore) {
        if( !initialized ) {
            throw new IllegalStateException( "dao is not initialized; call init() before calling this method" );
        }
        for( Score score : mathScores.getScores() ) {
            if( recenteredScore.equals(score.getRecenteredScore()) ) {
                return score.getPre1995score();
            }
        }

        return 0;
    }

    public Integer lookup1995VerbalScore(Integer recenteredScore) {
        if( !initialized ) {
            throw new IllegalStateException( "dao is not initialized; call init() before calling this method" );
        }
        for( Score score : verbalScores.getScores() ) {
            if( recenteredScore.equals(score.getRecenteredScore()) ) {
                return score.getPre1995score();
            }
        }

        return 0;
    }

    public Integer lookupRecenteredMathScore(Integer score1995) {
        if( !initialized ) {
            throw new IllegalStateException( "dao is not initialized; call init() before calling this method" );
        }
        for( Score score : mathScores.getScores() ) {
            if( score1995.equals(score.getPre1995score()) ) {
                return score.getRecenteredScore();
            }
        }

        return 0;
    }

    public Integer lookupRecenteredVerbalScore(Integer score1995) {

        if( !initialized ) {
            throw new IllegalStateException( "dao is not initialized; call init() before calling this method" );
        }

        for( Score score : verbalScores.getScores() ) {
            if( score1995.equals(score.getPre1995score()) ) {
                return score.getRecenteredScore();
            }
        }
        return 0;
    }

    public String getVerbalAttribution() {
        if( !initialized ) {
            throw new IllegalStateException( "dao is not initialized; call init() before calling this method" );
        }
        return verbalScores.getAttribution();
    }

    public String getMathAttribution() {
        if( !initialized ) {
            throw new IllegalStateException( "dao is not initialized; call init() before calling this method" );
        }
        return mathScores.getAttribution();
    }

    public boolean isInitialized() {
        return initialized;
    }
}
