/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2017, Gluon Software
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gluonhq.otn.model.cloudlink3;

import com.gluonhq.charm.glisten.afterburner.GluonView;
import com.gluonhq.cloudlink.client.data.DataClient;
import com.gluonhq.cloudlink.client.data.DataClientBuilder;
import com.gluonhq.cloudlink.client.data.OperationMode;
import com.gluonhq.connect.GluonObservableList;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.otn.model.BaseService;
import com.gluonhq.otn.model.EnabledOTNExperiences;
import com.gluonhq.otn.model.Exhibitor;
import com.gluonhq.otn.model.LatestClearThreeDModelVotes;
import com.gluonhq.otn.model.News;
import com.gluonhq.otn.model.Note;
import com.gluonhq.otn.model.OTN3DModel;
import com.gluonhq.otn.model.OTNCarvedBadgeOrder;
import com.gluonhq.otn.model.OTNCoffee;
import com.gluonhq.otn.model.OTNCoffeeOrder;
import com.gluonhq.otn.model.OTNEmbroidery;
import com.gluonhq.otn.model.OTNGame;
import com.gluonhq.otn.model.PushNotification;
import com.gluonhq.otn.model.Session;
import com.gluonhq.otn.model.Speaker;
import com.gluonhq.otn.model.Sponsor;
import com.gluonhq.otn.model.Venue;
import com.gluonhq.otn.model.Vote;
import com.gluonhq.otn.util.OTNSettings;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CloudLink3Service extends BaseService {

    private DataClient dataClient;

    private ReadOnlyListWrapper<Exhibitor> exhibitors = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    private ReadOnlyListWrapper<Session> sessions = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    private ReadOnlyListWrapper<Speaker> speakers = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    private ReadOnlyListWrapper<Sponsor> sponsors = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    private ReadOnlyListWrapper<Venue> venues = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    private ReadOnlyListWrapper<News> news = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    private PushNotification pushNotification = new PushNotification();

    private ReadOnlyObjectWrapper<EnabledOTNExperiences> enabledOTNExperiences;
    private ReadOnlyObjectWrapper<LatestClearThreeDModelVotes> latestClearVotes;
    private ReadOnlyListWrapper<OTNCoffee> otnCoffees;
    private ReadOnlyListWrapper<OTNGame> otnGames = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    private ReadOnlyListWrapper<OTNEmbroidery> otnEmbroideries = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    private ReadOnlyListWrapper<OTN3DModel> otn3DModels = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());

    public CloudLink3Service() {
        dataClient = DataClientBuilder.create()
                .operationMode(OperationMode.CLOUD_FIRST)
                .build();
    }

    @Override
    public ReadOnlyListProperty<News> retrieveNews() {
        return news.getReadOnlyProperty();
    }

    @Override
    public PushNotification retrievePushNotification() {
        return pushNotification;
    }

    @Override
    public ReadOnlyListProperty<Session> retrieveSessions() {
        return sessions.getReadOnlyProperty();
    }

    @Override
    public ReadOnlyListProperty<Speaker> retrieveSpeakers() {
        return speakers.getReadOnlyProperty();
    }

    @Override
    public ReadOnlyListProperty<Exhibitor> retrieveExhibitors() {
        return exhibitors.getReadOnlyProperty();
    }

    @Override
    public ReadOnlyListProperty<Sponsor> retrieveSponsors() {
        return sponsors.getReadOnlyProperty();
    }

    @Override
    public ReadOnlyListProperty<Venue> retrieveVenues() {
        return venues.getReadOnlyProperty();
    }

    @Override
    public void storeSurveyAnswers(String answers) {

    }

    @Override
    public boolean isSurveyCompleted() {
        return false;
    }

    @Override
    public void retrieveSurveyAnswers() {

    }

    @Override
    public ReadOnlyObjectProperty<EnabledOTNExperiences> retrieveEnabledOTNExperiences() {
        if (enabledOTNExperiences == null) {
            enabledOTNExperiences = new ReadOnlyObjectWrapper<>(new EnabledOTNExperiences());
            enabledOTNExperiences.get().badgeEnabledProperty().set(true);
            enabledOTNExperiences.get().coffeeEnabledProperty().set(true);
        }
        return enabledOTNExperiences.getReadOnlyProperty();
    }

    @Override
    public ReadOnlyListProperty<OTNCoffee> retrieveOTNCoffees() {
        if (otnCoffees == null) {
            GluonObservableList<OTNCoffee> gluonCoffees = DataProvider.retrieveList(dataClient.createFunctionListDataReader("otnCoffees", OTNCoffee.class));
            otnCoffees = new ReadOnlyListWrapper<>(gluonCoffees);
        }
        return otnCoffees.getReadOnlyProperty();
    }

    @Override
    public GluonObservableObject<OTNCoffeeOrder> orderOTNCoffee(OTNCoffee coffee, int strength) {
        return DataProvider.retrieveObject(dataClient.createFunctionObjectDataReader("otnOrderCoffee", OTNCoffeeOrder.class, coffee.getType(), String.valueOf(strength)));
    }

    @Override
    public GluonObservableObject<OTNCarvedBadgeOrder> orderOTNCarveABadge(String shape) {
        return DataProvider.retrieveObject(dataClient.createFunctionObjectDataReader("otnCarveBadge", OTNCarvedBadgeOrder.class, shape));
    }

    @Override
    public ReadOnlyListProperty<OTNGame> retrieveOTNGames() {
        return otnGames.getReadOnlyProperty();
    }

    @Override
    public ReadOnlyListProperty<OTNEmbroidery> retrieveOTNEmbroideries() {
        return otnEmbroideries.getReadOnlyProperty();
    }

    @Override
    public ReadOnlyListProperty<OTN3DModel> retrieveOTN3DModels() {
        return otn3DModels.getReadOnlyProperty();
    }

    @Override
    public boolean canVoteForOTN3DModel() {
        return latestClearVotes == null || latestClearVotes.get() == null || OTNSettings.getLastVoteCast() <= latestClearVotes.get().getTimestamp();
    }

    @Override
    public ReadOnlyObjectProperty<LatestClearThreeDModelVotes> retrieveLatestClearVotes() {
        if (latestClearVotes == null) {
            latestClearVotes = new ReadOnlyObjectWrapper<>();
            latestClearVotes.set(new LatestClearThreeDModelVotes());
            latestClearVotes.get().timestampProperty().set(0L);
        }
        return latestClearVotes;
    }

    @Override
    public void voteForOTN3DModel(String id) {

    }

    @Override
    public ObservableList<Session> internalRetrieveFavoriteSessions() {
        return FXCollections.observableArrayList();
    }

    @Override
    public ObservableList<Session> internalRetrieveScheduledSessions(Runnable onStateSucceeded) {
        return FXCollections.observableArrayList();
    }

    @Override
    public ObservableList<Note> internalRetrieveNotes() {
        return FXCollections.observableArrayList();
    }

    @Override
    public ObservableList<Vote> internalRetrieveVotes() {
        return FXCollections.observableArrayList();
    }

    @Override
    public GluonView getAuthenticationView() {
        return null;
    }
}
