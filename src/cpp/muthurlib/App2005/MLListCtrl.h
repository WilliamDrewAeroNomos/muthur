#if !defined(__ML_LISTCTRL_H__)
#define __ML_LISTCTRL_H__

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLDataObject.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------
class CMLListCtrlElement;
class CMLListCtrlElements;
class CMLListCtrlColumn;
class CMLListCtrlColumns;


#define DV_OBJECT_OWNED					0	


class CMLListCtrlData
{
protected:
	bool							m_bOwner;	//<! Indicates whether or not this federate owns an object
	CObList							m_apElements;
public:

									CMLListCtrlData();
	virtual						   ~CMLListCtrlData(){}

	virtual	CString					GetName(int iPropId);
	virtual CString					GetValue(int iPropId);
	virtual CMLDataObject*			GetDataObject(){ return NULL; }
	virtual CObList*				GetElements(){ return NULL; }
	virtual EMLDataObjectClass		GetClass(){ return ML_DATAOBJECT_CLASS_BASE; }

	virtual char*					GetACID(){ return ""; }
	virtual void					SetACID(char* pszACID){}

	virtual bool					GetOwnership(){ return m_bOwner; }
	virtual void					SetOwnership(bool b){ m_bOwner = b; }

	virtual void					Randomize(){};
	virtual void					Update(CMLDataObject* pData){};

	//virtual void					SetAircraftType(LPCSTR lpszAcType){};
	//virtual void					SetPublishLatitude(LPCSTR lpszLatitude){};
	//virtual void					SetPublishLongitude(LPCSTR lpszLongitude){};
	//virtual void					SetPublishAltitude(LPCSTR lpszAltitude){};
	//virtual void					SetPublishHeading(LPCSTR lpszHeading){};
	//virtual void					SetPublishPitch(LPCSTR lpszPitch){};
	//virtual void					SetPublishRoll(LPCSTR lpszRoll){};
	//virtual void					SetPublishSector(LPCSTR lpszSector){};
	//virtual void					SetPublishCenter(LPCSTR lpszCenter){};
	//virtual void					SetPublishOnGround(LPCSTR lpszOnGround){};
	//virtual void					SetPublishFreq(LPCSTR lpszFreq){};
	//virtual void					SetPublishSquawkCode(LPCSTR lpszSquawkCode){};
	//virtual void					SetPublishIdent(LPCSTR lpszIdent){};

};

class CMLListCtrlElement : public CObject
{
private:

	CMLListCtrlData*				m_pData;
	int								m_iOwner;

public:

	CMLListCtrlElement(CMLListCtrlData* pData = NULL);
	~CMLListCtrlElement();

	virtual CMLListCtrlData*			GetData(){ return m_pData; }
	virtual void						SetData(CMLListCtrlData* pData){ m_pData = pData; }

	virtual int						GetOwner(){ return m_iOwner; }
	virtual void						SetOwner(int iOwner){ m_iOwner = iOwner; }

	virtual	CMLListCtrlColumns*		GetListCtrlColumns();
	virtual void					GetListCtrlText(CMLListCtrlColumns* pColumns);
	virtual void					GetListCtrlText(CMLListCtrlColumn* pColumn);
	virtual int						GetListCtrlImage(){ return -1; }
	virtual int						GetListCtrlComparison(CMLListCtrlElement* pMLElement, int iColumn);

protected:

};

class CMLListCtrlElements : public CObList
{
private:

public:

	CMLListCtrlElements();
	~CMLListCtrlElements();

	BOOL				Add(CMLListCtrlElement* pMLElement);
	void				Flush(BOOL bDelete);
	void				Remove(CMLListCtrlElement* pMLElement, BOOL bDelete);
	POSITION			Find(CMLListCtrlElement* pMLElement);

	CMLListCtrlElement*	First();
	CMLListCtrlElement*	Last();
	CMLListCtrlElement*	Next();
	CMLListCtrlElement*	Prev();

protected:

	POSITION			m_NextPos;
	POSITION			m_PrevPos;
};

class CMLListCtrlColumn : public CObject
{
private:

public:

	CString				m_strName;
	CString				m_strText;
	int					m_iHeaderWidth;
	int					m_iIndex;
	int					m_iOwner;

	CMLListCtrlColumn();
	~CMLListCtrlColumn();

protected:

};

class CMLListCtrlColumns : public CObArray
{
private:

public:

	CMLListCtrlColumns();
	virtual			   ~CMLListCtrlColumns();

	void				Add(CMLListCtrlColumn* pColumn);
	void				Flush(BOOL bDelete);

	void				SetAt(int iIndex, CMLListCtrlColumn* pColumn);
	CMLListCtrlColumn*	GetAt(int iIndex);

protected:
};

class CMLListCtrl : public CListCtrl
{
private:

	CMLListCtrlColumns*	m_pColumns;
	BOOL				m_bSuppress;
	BOOL				m_bUseImages;
	CImageList			m_aImages;

public:


	CMLListCtrl();
	virtual			   ~CMLListCtrl();

	void				Clear();
	void				Add(CObList* pMLElements, BOOL bClear);
	void				AutoSizeColumns();
	void				Sort(int iColumn);
	void				Redraw(int iIndex);
	void				Redraw(CMLListCtrlElement* pMLElement);
	void				Select(int iIndex, BOOL bSuppress);
	void				Delete(int iIndex, BOOL bSuppress);
	BOOL				Select(CMLListCtrlElement* pMLElement, BOOL bSuppress = FALSE);
	BOOL				Delete(CMLListCtrlElement* pMLElement, BOOL bSuppress = FALSE);
	BOOL				Initialize(CMLListCtrlElement* pOwnerClass, UINT uResourceId = 0);
	BOOL				Add(CMLListCtrlElement* pMLElement, BOOL bResizeColumns = TRUE);
	BOOL				Insert(CMLListCtrlElement* pMLElement, int iIndex, BOOL bResizeColumns = TRUE);
	int					GetIndex(CMLListCtrlElement* pMLElement);
	CMLListCtrlElement*	GetSelection();

protected:

	void				AutoSizeColumn(int iIndex, CMLListCtrlColumn* pColumn);
	BOOL				Add(CMLListCtrlColumn* pColumn, int iIndex);

	static int CALLBACK DoComparison(LPARAM, LPARAM, LPARAM);

	//	The remainder of this declaration is maintained by Class Wizard
public:

	// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(MLList)
	//}}AFX_VIRTUAL

	// Generated message map functions
protected:
	//{{AFX_MSG(MLList)
	afx_msg void OnGetDispInfo(NMHDR* pNMHDR, LRESULT* pResult);
	afx_msg BOOL OnItemChanged(NMHDR* pNMHDR, LRESULT* pResult);
	afx_msg BOOL OnItemChanging(NMHDR* pNMHDR, LRESULT* pResult);
	//}}AFX_MSG

	DECLARE_MESSAGE_MAP()
};

#endif // !defined(__ML_LISTCTRL_H__)
