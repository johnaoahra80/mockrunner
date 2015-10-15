package com.mockrunner.jdbc;

import com.mockrunner.util.common.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Helper class for finding matching SQL statements based on various
 * search parameters. The search parameters are:
 * <br>
 * <code>caseSensitive</code> do a case sensitive match (default is <code>false</code>)
 * <br>
 * <code>exactMatch</code> the strings must match exactly, the parameter <code>caseSensitive</code>
 *                         is recognized, but <code>useRegularExpression</code> is irrelevant,
 *                         if <code>exactMatch</code> is <code>true</code> (default is <code>false</code>)
 * <br>
 * <code>useRegularExpression</code> use regular expressions for matching, if this parameter is
 *                                   <code>false</code>, strings match, if one string starts with the other
 *                                   (default is <code>false</code>)
 */
public class SQLStatementMatcher<T>
{
    private boolean caseSensitive = false;
    private boolean exactMatch = false;
    private boolean useRegularExpressions = false;
    
    public SQLStatementMatcher(boolean caseSensitive, boolean exactMatch)
    {
        this(caseSensitive, exactMatch, false);
    }
    
    public SQLStatementMatcher(boolean caseSensitive, boolean exactMatch, boolean useRegularExpressions)
    {
        this.caseSensitive = caseSensitive;
        this.exactMatch = exactMatch;
        this.useRegularExpressions = useRegularExpressions;
    }
    
    /**
     * Compares all keys in the specified <code>Map</code> with the
     * specified query string using the method {@link #doStringsMatch}.
     * If the strings match, the corresponding object from the <code>Map</code>
     * is added to the resulting <code>List</code>.
     * @param dataMap the source <code>Map</code>
     * @param query the query string that must match the keys in <i>dataMap</i>
     * @param queryContainsMapData only matters if <i>isExactMatch</i> is <code>false</code>,
     *        specifies if query must be contained in the <code>Map</code> keys (<code>false</code>)
     *        or if query must contain the <code>Map</code> keys (<code>true</code>)
     * @return the result <code>List</code>
     */
    public List<T> getMatchingObjects(Map<String, ? extends T> dataMap, String query, boolean queryContainsMapData)
	{
		if(null == query) query = "";
		List<T> resultList = new ArrayList<T>();
        
        for(Entry<String, ? extends T> entry : dataMap.entrySet()){
			String source, currentQuery;
			if(queryContainsMapData)
			{
				source = query;
				currentQuery = entry.getKey();
			}
			else
			{
				source = entry.getKey();
				currentQuery = query;
			}
			if(doStringsMatch(source, currentQuery)){
                T matchingObject = entry.getValue();
    			resultList.add(matchingObject);
            }
        }
		return resultList;
	}
    
    /**
     * Compares all keys in the specified <code>Map</code> with the
     * specified query string using the method {@link #doStringsMatch}.
     * If the strings match, the corresponding object from the <code>Map</code>
     * is added to the resulting <code>List</code>.
     * @param dataMap the source <code>Map</code>
     * @param query the query string that must match the keys in <i>dataMap</i>
     * @param queryContainsMapData only matters if <i>isExactMatch</i> is <code>false</code>,
     *        specifies if query must be contained in the <code>Map</code> keys (<code>false</code>)
     *        or if query must contain the <code>Map</code> keys (<code>true</code>)
     * @return the result <code>List</code>
     */
    public List<T> getMatchingObjectsFromCollections(Map<String, ? extends Collection<? extends T>> dataMap, String query, boolean queryContainsMapData)
	{
		if(null == query) query = "";
		List<T> resultList = new ArrayList<T>();
        
        for(Entry<String, ? extends Collection<? extends T>> entry : dataMap.entrySet()){
			String source, currentQuery;
			if(queryContainsMapData)
			{
				source = query;
				currentQuery = entry.getKey();
			}
			else
			{
				source = entry.getKey();
				currentQuery = query;
			}
			if(doStringsMatch(source, currentQuery)){
                Collection<? extends T> matchingObject = entry.getValue();
                resultList.addAll( matchingObject );
            }
        }
		return resultList;
	}
    
    /**
     * Compares all elements in the specified <code>Collection</code> with the
     * specified query string using the method {@link #doStringsMatch}.
     * @param col the <code>Collections</code>
     * @param query the query string that must match the keys in <i>col</i>
     * @param queryContainsData only matters if <i>exactMatch</i> is <code>false</code>,
     *        specifies if query must be contained in the <code>Collection</code> data (<code>false</code>)
     *        or if query must contain the <code>Collection</code> data (<code>true</code>)
     * @return <code>true</code> if <i>col</i> contains <i>query</i>, false otherwise
     */
    public boolean contains(Collection<String> col, String query, boolean queryContainsData)
    {
        for(String element : col){
			String source, currentQuery;
			if(queryContainsData)
			{
				source = query;
				currentQuery = element;
			}
			else
			{
				source = element;
				currentQuery = query;
			}
			if(doStringsMatch(source, currentQuery)) return true;
        }
        return false;
    }
    
    /**
     * Compares two strings and returns if they match. 
     * @param query the query string that must match source
     * @param source the source string
     * @return <code>true</code> of the strings match, <code>false</code> otherwise
     */
    public boolean doStringsMatch(String source, String query)
    {
        if(null == source) source = "";
        if(null == query) query = "";
        if(useRegularExpressions && !exactMatch)
        {
            return doRegExMatch( source, query );
        }
        else
        {
            return doSimpleMatch(source, query);
        }
    }

    private boolean doSimpleMatch(String source, String query)
    {
        if(exactMatch)
        {
            return StringUtil.matchesExact(source, query, caseSensitive);
        }
        else
        {
            return StringUtil.matchesContains(source, query, caseSensitive);
        }
    }

    private boolean doRegExMatch(String source, String query) {
        return StringUtil.matchesRegex(source, query, caseSensitive);
    }
}
